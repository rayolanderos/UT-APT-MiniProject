import webapp2
import json
import logging
from google.appengine.ext import ndb
from google.appengine.api import images

from models.stream import Stream

class View(webapp2.RequestHandler):

    def post(self):
        
        json_string = self.request.body

        dict_object = json.loads(json_string)

        stream_id = int(dict_object['id'])

        self.response.headers['Content-Type'] = 'application/json'
        stream = Stream.get_by_id(stream_id)
        
        if stream != None :
            photos_urls = [images.get_serving_url(photo_key) for photo_key in stream.photos]
            stream_data = {
            'id': stream.key.id(), 
            'name': stream.name, 
            'cover_url': stream.cover_url,
            'photos': photos_urls }
        else:
            stream_data = {
            'id': None }


        self.response.out.write(json.dumps(stream_data))
