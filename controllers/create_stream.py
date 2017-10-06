import webapp2
import jinja2
import json
import os
import logging

from google.appengine.api import urlfetch
from google.appengine.api import users
from google.appengine.api import mail
from google.appengine.api import app_identity

templates_dir = os.path.normpath(os.path.dirname(__file__) + '/../www/')

JINJA_ENVIRONMENT = jinja2.Environment(
    loader=jinja2.FileSystemLoader(templates_dir),
    extensions=['jinja2.ext.autoescape'],
    autoescape=True)


DEFAULT_EMAIL_MESSAGE = 'Hi! Please subscribe to my new connexus stream.'

class Create(webapp2.RequestHandler):

    def post(self):
        user = users.get_current_user()
        email_address = user.email()

        stream_name = self.request.get('stream-name')
        invite_message = self.request.get('email-message', DEFAULT_EMAIL_MESSAGE)
        subs_emails = self.request.get_all('subscriber-emails')
        stream_tags = self.request.get_all('stream-tags')
        cover_url = self.request.get('stream-cover-url')
        stream_data = { 'name': stream_name,
                        'emails': subs_emails, 
                        'email_message': invite_message, 
                        'tags': stream_tags,
                        'cover_url': cover_url, 
                        'owner': user.user_id() }

        
        create_api_uri = self.uri_for('api-create-stream', _full=True)

        result = urlfetch.fetch(
            url=create_api_uri,
            payload=json.dumps(stream_data),
            method=urlfetch.POST,
            headers= {'Content-Type': 'application/json'}
        )
        if result.status_code == 200:
            response = json.loads(result.content)
            user = users.get_current_user()
            
            if response['success'] : 
                self.send_invitation_emails(email_address, subs_emails, response['stream_url'], invite_message)
                self.redirect('/manage')
            else: 
                logout_url = users.create_logout_url('/')
                page_data = { 
                    'logout_url': logout_url, 
                    'page_name': 'create',
                    'error_msg': response['msg']
                }
                template = JINJA_ENVIRONMENT.get_template('error.html')
                self.response.write(template.render(page_data))

    def get(self):
        user = users.get_current_user()
        template = JINJA_ENVIRONMENT.get_template('create.html')
        logout_url = users.create_logout_url('/')
        page_data = { 
                    'logout_url': logout_url, 
                    'page_name': 'create', 
                    'user': user }
        self.response.write(template.render(page_data))


    def send_invitation_emails(self, sender, emails, stream_url, message):

        email_content = '{0}\nYou can find the stream here: {1}'.format(message, stream_url)
        html_email_content = '{0}\nYou can find the stream <a href="{1}">here.</a>'.format(message, stream_url)

        for email in emails:
            email_message = mail.EmailMessage(
                sender=sender,
                subject='Subscribe to my Connexus stream'
            )

            email_message.to = email
            email_message.body = email_content
            email_message.html = html_email_content
            email_message.send()


        pass