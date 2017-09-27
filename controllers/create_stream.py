import webapp2
import jinja2
import json
import os
import logging

from google.appengine.api import urlfetch

templates_dir = os.path.normpath(os.path.dirname(__file__) + '/../www/')

JINJA_ENVIRONMENT = jinja2.Environment(
    loader=jinja2.FileSystemLoader(templates_dir),
    extensions=['jinja2.ext.autoescape'],
    autoescape=True)

class Create(webapp2.RequestHandler):

    def post(self):
        stream_name = self.request.get('stream-name')
        invite_message = self.request.get('email-message')
        subs_emails = self.request.get_all('subscriber-emails')
        stream_tags = self.request.get_all('stream-tags')
        cover_url = self.request.get('stream-cover-url')
        stream_data = { 'name': stream_name,
                        'emails': subs_emails, 
                        'email_message': invite_message, 
                        'tags': stream_tags,
                        'cover_url': cover_url}

        
        create_api_uri = self.uri_for('api-create-stream', _full=True)
        logging.info(create_api_uri)

        result = urlfetch.fetch(
            url=create_api_uri,
            payload=json.dumps(stream_data),
            method=urlfetch.POST,
            headers= {'Content-Type': 'application/json'}
        )

        logging.info('Post Results: %s', result)
        self.redirect('/manage')

    def get(self):
        template = JINJA_ENVIRONMENT.get_template('create.html')
        self.response.write(template.render({}))