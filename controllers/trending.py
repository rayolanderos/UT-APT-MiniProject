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

class Trending(webapp2.RequestHandler):

    def get(self):
        user = users.get_current_user()
        trending_api_uri = self.uri_for('api-trending', _full=True)

        result = urlfetch.fetch(url = trending_api_uri)

        if result.status_code == 200:
            logout_url = users.create_logout_url('/')

            j = json.loads(result.content)
            page_data = { 
                'streams' : j,
                'user': user, 
                'logout_url': logout_url, 
                'page_name': 'trending'
            }
            template = JINJA_ENVIRONMENT.get_template('trending.html')
            self.response.write(template.render(page_data))

    def post(self):
        #TODO : Handle report update
        user = users.get_current_user()

        logout_url = users.create_logout_url('/')
        page_data = {
            'user': user, 
            'logout_url': logout_url, 
            'page_name': 'trending'
        }
        template = JINJA_ENVIRONMENT.get_template('trending.html')
        self.response.write(template.render(page_data))