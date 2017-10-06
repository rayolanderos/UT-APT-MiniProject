import webapp2
import jinja2
import json
import os
import logging

from models.connexus_user import ConnexusUser
from models.stream import Stream

from google.appengine.api import urlfetch
from google.appengine.api import users
from google.appengine.ext import blobstore

templates_dir = os.path.normpath(os.path.dirname(__file__) + '/../www/')

JINJA_ENVIRONMENT = jinja2.Environment(
    loader=jinja2.FileSystemLoader(templates_dir),
    extensions=['jinja2.ext.autoescape'],
    autoescape=True)

class View(webapp2.RequestHandler):

    def display_error(self, error_message):
        page_data = { 
            'logout_url': users.create_logout_url('/'), 
            'page_name': 'view',
            'error_msg': error_message
        }
        template = JINJA_ENVIRONMENT.get_template('error.html')
        self.response.write(template.render(page_data))

    def is_user_subscribed(self, stream_id):
        user = users.get_current_user()
        stream = Stream.get_by_id(stream_id)
        return ConnexusUser.is_subscribed(user.user_id(), stream.key)

    def get(self):
        user = users.get_current_user()
        stream_id = long(self.request.get('id'))
        limit = int(self.request.get('limit', '10'))
        logout_url = users.create_logout_url('/')

        if not stream_id :
            self.display_error('The stream you are trying to access does not exist. It may have been removed by the owner.')
        else : 
            view_api_uri = '{}?id={}&offset=0&limit={}'.format(self.uri_for('api-view', _full=True), stream_id, limit)
            result = urlfetch.fetch(url = view_api_uri)

            subscribe_url = self.uri_for('subscribe-stream', _full=True)
            if self.is_user_subscribed(stream_id):
                subscribe_url = self.uri_for('unsubscribe-stream', _full=True)

            if result.status_code == 200:
                j = json.loads(result.content)
                if not j.get('id'):
                    self.display_error('The stream you are trying to access does not exist. It may have been removed by the owner.')
                else:
                    upload_url = blobstore.create_upload_url('/upload_photo')
                    page_data = {
                        'stream': j,
                        'logout_url': logout_url, 
                        'page_name': 'view',
                        'upload_url': upload_url,
                        'limit' : limit + 10,
                        'is_subscribed': self.is_user_subscribed(stream_id),
                        'subscribe_url': subscribe_url,
                    }
                    template = JINJA_ENVIRONMENT.get_template('view-single.html')
                    self.response.write(template.render(page_data))
