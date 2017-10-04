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


DEFAULT_EMAIL_MESSAGE = 'Hi! Plase subscribe to my new connexus stream.'

class Create(webapp2.RequestHandler):

    def post(self):
        user = users.get_current_user()
        email_address = user.email()
        logging.info(email_address)

        stream_name = self.request.get('stream-name')
        invite_message = self.request.get('email-message', DEFAULT_EMAIL_MESSAGE)
        subs_emails = self.request.get_all('subscriber-emails')
        stream_tags = self.request.get_all('stream-tags')
        cover_url = self.request.get('stream-cover-url')
        stream_data = { 'name': stream_name,
                        'emails': subs_emails, 
                        'email_message': invite_message, 
                        'tags': stream_tags,
                        'cover_url': cover_url}

        # search API
        id = stream.key.id() # need to get the id here
        document = search.Document(
            doc_id=id,
            fields=[
                search.TextField(name='stream-id', value=id),
                search.TextField(name='stream-name', value=stream_name),
                search.TextField(name='email-message', value=invite_message),
                search.TextField(name='subsciber-emails', value=subs_emails),
                search.TextField(name='stream-tags', value=stream_tags),
                search.DateField(name='stream-cover-url', value=cover_url),
            ])
        index = search.Index('streams', namespace='namespace') # temporary as don't know current namespace if created
        index.put(document)


        create_api_uri = self.uri_for('api-create-stream', _full=True)

        result = urlfetch.fetch(
            url=create_api_uri,
            payload=json.dumps(stream_data),
            method=urlfetch.POST,
            headers= {'Content-Type': 'application/json'}
        )

        self.send_invitation_emails(email_address, subs_emails, result.headers['location'], invite_message)
        self.redirect('/manage')

    def get(self):
        template = JINJA_ENVIRONMENT.get_template('create.html')
        self.response.write(template.render({}))


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