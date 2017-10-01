import webapp2
import jinja2
import json
import os
import logging

from google.appengine.api import urlfetch
from google.appengine.api import users
from google.appengine.ext import blobstore

templates_dir = os.path.normpath(os.path.dirname(__file__) + '/../www/')

JINJA_ENVIRONMENT = jinja2.Environment(
    loader=jinja2.FileSystemLoader(templates_dir),
    extensions=['jinja2.ext.autoescape'],
    autoescape=True)

class View(webapp2.RequestHandler):

    def get(self):

        stream_id = self.request.get('id')
        logout_url = users.create_logout_url('/')

        if not stream_id :
            page_data = { 
                'logout_url': logout_url, 
                'page_name': 'view',
                'error_msg': 'The stream you are trying to access does not exist. It may have been removed by the owner.'
            }
            template = JINJA_ENVIRONMENT.get_template('error.html')
            self.response.write(template.render(page_data))
        else : 
            view_api_uri = self.uri_for('api-view', _full=True)
            stream_data = { 'id': stream_id }
            json_data = json.dumps(stream_data);

            result = urlfetch.fetch(
                url = view_api_uri, 
                payload=json_data,
                method=urlfetch.POST,
                headers= {'Content-Type': 'application/json'})

            if result.status_code == 200:
                j = json.loads(result.content)
                if not j.get('id'):
                    page_data = { 
                        'logout_url': logout_url, 
                        'page_name': 'view',
                        'error_msg': 'The stream you are trying to access does not exist. It may have been removed by the owner.'
                    }
                    template = JINJA_ENVIRONMENT.get_template('error.html')
                    self.response.write(template.render(page_data))
                else:
                    upload_url = blobstore.create_upload_url('/upload_photo')
                    page_data = {
                        'stream': j, 
                        'logout_url': logout_url, 
                        'page_name': 'view',
                        'upload_url': upload_url
                    }
                    template = JINJA_ENVIRONMENT.get_template('view-single.html')
                    self.response.write(template.render(page_data))

    def post(self):
        # TODO handle
        pass