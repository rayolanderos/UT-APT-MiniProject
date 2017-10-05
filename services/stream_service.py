import webapp2
import json
import logging
import datetime
import random
import urllib

from models.connexus_user import ConnexusUser
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
            stream_id = str(stream_key.id())

            #Search indexing
            latitude = random.uniform(90, -90)
            longitude = random.uniform(180, -180)
            geopoint = search.GeoPoint(latitude, longitude)
            search_tags = ' '.join(tags)
            search_index = search.Document(
                doc_id= stream_id,
                fields=[search.TextField(name='name', value=stream_name),
                search.TextField(name='cover_url', value=stream_cover_url), 
                search.TextField(name='tags', value=search_tags),
                search.TextField(name="stream_id", value= stream_id),
                search.DateField(name='date', value=datetime.datetime.now()), 
                search.GeoField(name='stream_location', value=geopoint) ])
            result = search.Index(name='stream').put(search_index)

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
        
        logging.info(json_string)

        user_id = long(dict_object['user_id'])
        stream_id = long(dict_object['stream_id'])
        user = ConnexusUser.get_by_id(user_id)
        stream = Stream.get_by_id(stream_id)

        if user:
            user.streams_subscribed.append(stream.key)
            user.put()
        else:
            logging.error('User not found!')

        #Send email to owner??

class Unsubscribe(webapp2.RequestHandler):
    def post(self):
        json_string = self.request.body
        dict_object = json.loads(json_string)

        user_id = long(dict_object['user_id'])
        stream_id = long(dict_object['stream_id'])
        user = ConnexusUser.get_by_id(user_id)
        stream = Stream.get_by_id(stream_id)

        if user:
            user.streams_subscribed = filter(lambda s: s !=stream.key, user.streams_subscribed)
            user.put()
        else:
            logging.error('User not found!')