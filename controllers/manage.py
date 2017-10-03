import webapp2
import jinja2
import json
import os
import logging

from google.appengine.api import urlfetch
from google.appengine.api import users

templates_dir = os.path.normpath(os.path.dirname(__file__) + '/../www/')

JINJA_ENVIRONMENT = jinja2.Environment(
    loader=jinja2.FileSystemLoader(templates_dir),
    extensions=['jinja2.ext.autoescape'],
    autoescape=True)

class Manage(webapp2.RequestHandler):

    def get(self):
        user = users.get_current_user()
        manage_api_uri = '{}?type=own&user={}'.format(self.uri_for('api-manage', _full=True), user)
           
        result = urlfetch.fetch(url = manage_api_uri)

        if result.status_code == 200:
            logout_url = users.create_logout_url('/')
            j = json.loads(result.content)
            page_data = {
            'streams_own': j, 
            'streams_subscribe': j, 
            'logout_url': logout_url, 
            'page_name': 'manage'
            }
            template = JINJA_ENVIRONMENT.get_template('manage.html')
            self.response.write(template.render(page_data))

    def post(self):
        
        stream_delete = self.request.get_all('delete-stream')

        delete_api_uri = self.uri_for('api-delete-stream', _full=True)

        result = urlfetch.fetch(
            url=delete_api_uri,
            payload=json.dumps(stream_delete),
            method=urlfetch.POST,
            headers= {'Content-Type': 'application/json'}
        )


        user = users.get_current_user()
        manage_api_uri = '{}?type=own&user={}'.format(self.uri_for('api-manage', _full=True), user)
        result = urlfetch.fetch(url = manage_api_uri)

        if result.status_code == 200:
            logout_url = users.create_logout_url('/')
            j = json.loads(result.content)
            page_data = {
            'streams_own': j, 
            'streams_subscribe': j, 
            'logout_url': logout_url, 
            'page_name': 'manage'
            }
            template = JINJA_ENVIRONMENT.get_template('manage.html')
            self.response.write(template.render(page_data))
