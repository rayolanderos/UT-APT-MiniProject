import webapp2
import jinja2
import json
import os
import logging

from google.appengine.api import urlfetch
from google.appengine.api import users

from models.connexus_user import ConnexusUser

templates_dir = os.path.normpath(os.path.dirname(__file__) + '/../www/')

JINJA_ENVIRONMENT = jinja2.Environment(
    loader=jinja2.FileSystemLoader(templates_dir),
    extensions=['jinja2.ext.autoescape'],
    autoescape=True)

class Trending(webapp2.RequestHandler):

    def get(self):
        user = users.get_current_user()
        report_rate = 0 #TODO : Fetch the actual report rate for display purposes
        trending_api_uri = self.uri_for('api-trending', _full=True)

        result = urlfetch.fetch(url = trending_api_uri)

        if result.status_code == 200:
            logout_url = users.create_logout_url('/')
            user = ConnexusUser.from_user_id(user.user_id())
            report_rate = user.get_report_rate()

            j = json.loads(result.content)
            page_data = { 
                'streams' : j,
                'user': user, 
                'report_rate' : str(report_rate),
                'logout_url': logout_url, 
                'page_name': 'trending'
            }
            template = JINJA_ENVIRONMENT.get_template('trending.html')
            self.response.write(template.render(page_data))

    def post(self):
        #TODO : Handle report update
        user = users.get_current_user()
        report_rate = self.request.get('report-rate')

        update_data = { 'user_id': user.user_id(),
                        'report_rate': report_rate
        } 

        update_user_report_api = self.uri_for('api-update-user-report', _full=True)

        result_updating = urlfetch.fetch(
            url=update_user_report_api,
            payload=json.dumps(update_data),
            method=urlfetch.POST,
            headers= {'Content-Type': 'application/json'}
        )

        trending_api_uri = self.uri_for('api-trending', _full=True)

        result_trending = urlfetch.fetch(url = trending_api_uri)

        if result_trending.status_code == 200:
            logout_url = users.create_logout_url('/')

            j = json.loads(result_trending.content)
            page_data = { 
                'streams' : j,
                'user': user, 
                'logout_url': logout_url, 
                'report_rate' : report_rate,
                'page_name': 'trending'
            }
            template = JINJA_ENVIRONMENT.get_template('trending.html')
            self.response.write(template.render(page_data))