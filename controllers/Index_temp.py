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

class Delete(webapp2.RequestHandler):

    def post(self):
        # code to delete stream here

        json_string = self.request.body

        dict_object = json.loads(json_string)

        stream_id = int(dict_object['id'])

        self.response.headers['Content-Type'] = 'application/json'
        stream = Stream.get_by_id(stream_id)
        if stream != None:
            id = stream.key.id()
            index = search.Index('streams',
                                 namespace='namespace')  # temporary as don't know current namespace if created
            index.delete(id)

        else:
            stream_data = {
                'id': None}


#Is namespace already created?

# from google.appengine.api import namespace_manager

# Called only if the current namespace is not set.
# def namespace_manager_default_namespace_for_request():
#     # The returned string will be used as the Google Apps domain.
#     return namespace_manager.google_apps_namespace()


# search API
# id = stream.key.id()  # need to get the id here
# document = search.Document(
#     doc_id=id,
#     fields=[
#         search.TextField(name='stream-id', value=id),
#         search.TextField(name='stream-name', value=stream_name),
#         search.TextField(name='email-message', value=invite_message),
#         search.TextField(name='subsciber-emails', value=subs_emails),
#         search.TextField(name='stream-tags', value=stream_tags),
#         search.DateField(name='stream-cover-url', value=cover_url),
#     ])
# index = search.Index('streams', namespace='namespace')  # temporary as don't know current namespace if created
# index.put(document)
