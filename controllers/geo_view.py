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

class GeoView(webapp2.RequestHandler):

    def display_error(self, error_message):
        page_data = { 
            'logout_url': users.create_logout_url('/'), 
            'page_name': 'view',
            'error_msg': error_message
        }
        template = JINJA_ENVIRONMENT.get_template('error.html')
        self.response.write(template.render(page_data))

    def get(self):
        user = users.get_current_user()
        stream_id = long(self.request.get('id'))
        logout_url = users.create_logout_url('/')

        if not stream_id :
            self.display_error('The geo view for the stream you are trying to access does not exist. It may have been removed by the owner.')
        else : 
            geo_view_api = '{}?id={}'.format(self.uri_for('api-geo-view', _full=True), stream_id)
            result = urlfetch.fetch(url = geo_view_api)

            if result.status_code == 200:
                photos = json.loads(result.content)
                page_data = {
                    'photos': photos,
                    'stream_id': stream_id,
                    'logout_url': logout_url, 
                    'page_name': 'geo_view'
                }
                template = JINJA_ENVIRONMENT.get_template('geo-view.html')
                self.response.write(template.render(page_data))
