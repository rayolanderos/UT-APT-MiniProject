import webapp2
import jinja2
import json
import os
import logging

from models.connexus_user import ConnexusUser

from google.appengine.api import urlfetch
from google.appengine.api import users
from google.appengine.ext import blobstore

class Subscribe(webapp2.RequestHandler):
    def post(self):
        user = users.get_current_user()

        user = ConnexusUser.from_user_id(user.user_id())
        stream_id = self.request.get('stream_id')
    
        subscribe_api_uri = self.uri_for('api-subscribe-stream', _full=True)
        subscription_data = {'user_id': str(user.key.id()), 'stream_id': stream_id}

        result = urlfetch.fetch(
            url=subscribe_api_uri,
            payload=json.dumps(subscription_data),
            method=urlfetch.POST,
            headers= {'Content-Type': 'application/json'}
        )

        if result.status_code == 200:
            self.redirect('{}?id={}'.format(self.uri_for('view', _full=True), stream_id))
        else:
            logging.error('Impossible to subscribe. User: {}, Stream: {}'.format(user, stream_id))

class Unsubscribe(webapp2.RequestHandler):
    def post(self):
        user = users.get_current_user()

        user = ConnexusUser.from_user_id(user.user_id())
        stream_id = self.request.get('stream_id')
    
        subscribe_api_uri = self.uri_for('api-unsubscribe-stream', _full=True)
        subscription_data = {'user_id': str(user.key.id()), 'stream_ids': [stream_id]}

        result = urlfetch.fetch(
            url=subscribe_api_uri,
            payload=json.dumps(subscription_data),
            method=urlfetch.POST,
            headers= {'Content-Type': 'application/json'}
        )

        if result.status_code == 200:
            self.redirect('{}?id={}'.format(self.uri_for('view', _full=True), stream_id))
        else:
            logging.error('Impossible to unsubscribe. User: {}, Stream: {}'.format(user, stream_id))