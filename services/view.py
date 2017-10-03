import webapp2
import json
import logging
import datetime

from google.appengine.ext import ndb
from google.appengine.api import images


from models.stream import Stream

class View(webapp2.RequestHandler):

    def get(self):
        stream_id = int(self.request.get('id'))

        self.response.headers['Content-Type'] = 'application/json'
        stream = Stream.get_by_id(stream_id)
        now = datetime.datetime.now()
        
        if stream != None :
            if not stream.views :
                stream.views = 0
                
            if not stream.views_list :
                stream.views_list = []
                
            stream.views = stream.views+1
            stream.views_list.insert(0, now)
            stream.put()
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
