import webapp2
import jinja2
import json
import os
import logging

from models.connexus_user import ConnexusUser

from google.appengine.api import urlfetch
from google.appengine.api import users

templates_dir = os.path.normpath(os.path.dirname(__file__) + '/../www/')

JINJA_ENVIRONMENT = jinja2.Environment(
    loader=jinja2.FileSystemLoader(templates_dir),
    extensions=['jinja2.ext.autoescape'],
    autoescape=True)

class Manage(webapp2.RequestHandler):

    def get_user_streams(self, user):
        manage_api_uri = '{}?type=own&user={}'.format(self.uri_for('api-manage', _full=True), user.user_id())
        result = urlfetch.fetch(url = manage_api_uri)
        if result.status_code == 200:
            return json.loads(result.content)
        else:
            return []

    def get_user_subscriptions(self, user):
        manage_api_uri = '{}?type=subscribed&user={}'.format(self.uri_for('api-manage', _full=True), user.user_id())
        result = urlfetch.fetch(url = manage_api_uri)
        if result.status_code == 200:
            return json.loads(result.content)
        else:
            return []

    def get(self):
        user = users.get_current_user()

        logout_url = users.create_logout_url('/')
        page_data = {
            'streams_own': self.get_user_streams(user),
            'streams_subscribed': self.get_user_subscriptions(user),
            'logout_url': logout_url, 
            'page_name': 'manage'
        }
        template = JINJA_ENVIRONMENT.get_template('manage.html')
        self.response.write(template.render(page_data))

    def post(self):
        user = users.get_current_user()
        user = ConnexusUser.from_user_id(user.user_id())

        stream_delete = self.request.get_all('delete-stream')
        if len(stream_delete) > 0:
            delete_api_uri = self.uri_for('api-delete-stream', _full=True)

            result = urlfetch.fetch(
                url=delete_api_uri,
                payload=json.dumps(stream_delete),
                method=urlfetch.POST,
                headers= {'Content-Type': 'application/json'}
            )

        streams_unsubscribe = self.request.get_all('unsubscribe-stream')
        if len(streams_unsubscribe) > 0:                
            unsubscribe_url = self.uri_for('api-unsubscribe-stream', _full=True)
            subscription_data = {'user_id': str(user.key.id()), 'stream_ids': streams_unsubscribe}

            result = urlfetch.fetch(
                url=unsubscribe_url,
                payload=json.dumps(subscription_data),
                method=urlfetch.POST,
                headers={'Content-Type': 'application/json'}
            )

        self.redirect(self.uri_for('manage'))
