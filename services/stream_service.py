import webapp2
import json
import logging
import datetime

from google.appengine.api import search
from models.stream import Stream

class CreateStream(webapp2.RequestHandler):

    def post(self):

        json_string = self.request.body
        dict_object = json.loads(json_string)

        stream_name = dict_object['name']
        stream_cover_url = dict_object['cover_url']
        tags = dict_object['tags']
        emails = dict_object['emails']
        email_message = dict_object['email_message']
        owner = dict_object['owner']

        same_name = Stream.query(Stream.name == stream_name).fetch()

        if not same_name:

            #NDB storing
            stream = Stream(name=stream_name, cover_url=stream_cover_url, tags=tags, photos=[], owner=owner, views=0, views_list=[])
            stream_key = stream.put()

            #Search indexing
            search_index = search.Document(
                fields=[search.TextField(name='name', value=stream_name),
                search.TextField(name='cover_url', value=stream_cover_url), #search.TextField(name='tags', value=tags),
                search.TextField(name="stream_id", value= str(stream_key.id())),
                search.DateField(name='date', value=datetime.datetime.now() )])
            search.Index(name='stream').put(search_index)

            stream_url = '/view?id={0}'.format(stream_key.id() )
            res = { "msg" : "Stream Created", "success": True, "stream_url" : stream_url }
            self.response.out.write(json.dumps(res))
            # self.response.location = '/view?id={0}'.format(stream_key.id())
        else:
            res = { "msg" : "You tried to create a stream whose name is the same as an existing stream. The operation did not complete.", "success": False }
            self.response.out.write(json.dumps(res))

class Subscribe(webapp2.RequestHandler):
    def post(self):
        json_string = self.request.body
        dict_object = json.loads(json_string)
        
        user_id = dict_object['user_id']
        stream_id = dict_object['stream_id']
        user = ConnexusUser.query(ConnexusUser.user_id() == user_id).fetch()
        stream = Stream.query(Stream.key().id() == stream_id).fetch() #test

        #TODO handle the actual subscription in the model
        #Send email to owner??

class Unsubscribe(webapp2.RequestHandler):
    def post(self):
        pass