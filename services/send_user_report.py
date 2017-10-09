import webapp2
import json
import datetime
import logging

from google.appengine.api import urlfetch
from google.appengine.api import mail
from google.appengine.api import app_identity

from models.connexus_user import ConnexusUser

class SendUserReport(webapp2.RequestHandler):

    def get(self):
        self.response.headers['Content-Type'] = 'application/json'
        rate = self.request.get('rate')
        report_rate = int(rate)
        email_address = "noreply@ut-apt-miniproject-greenteam.appspotmail.com"
        email_subject = "This is your requested digest for connexus"

        subscribers = ConnexusUser.query(ConnexusUser.report == report_rate).fetch()

        subs_emails = []

        for subscriber in subscribers:
            subs_emails.append(subscriber.user_email)

        trending_api_uri = self.uri_for('api-trending', _full=True)

        result = urlfetch.fetch(url = trending_api_uri)

        if result.status_code == 200:
            trending_streams = json.loads(result.content)
            message = "These are the trending streams in the last hour:"
            html_message = "<h1>These are the trending streams in the last hour:</h1>"
            for trending_stream in trending_streams:
                message += "\n {0} ({1} views)".format(trending_stream['name'], trending_stream['views_list_count'])
                html_message += "<br> {0} (<b>{1}</b> views)".format(trending_stream['name'], trending_stream['views_list_count'])
            email_content = message
            html_email_content = html_message

            self.send_report(email_address, subs_emails, email_subject, email_content, html_email_content)

        res = { "msg" : "msgs sent", "success": True }

        self.response.out.write(json.dumps(res))


    def send_report(self, sender, emails, subject, email_content, html_email_content):
        
        for email in emails:
            
            email_message = mail.EmailMessage(
                sender=sender,
                subject= subject
            )
            email_message.to = email
            email_message.body = email_content
            email_message.html = html_email_content
            email_message.send()
        pass
