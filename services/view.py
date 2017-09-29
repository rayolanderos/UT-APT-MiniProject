import webapp2
import json
import logging
from google.appengine.ext import ndb

from models.stream import Stream

class View(webapp2.RequestHandler):

    def post(self):
        
        json_string = self.request.body

        dict_object = json.loads(json_string)

        stream_id = int(dict_object['id'])

        self.response.headers['Content-Type'] = 'application/json'
        stream = Stream.get_by_id(stream_id)
        if stream != None : 
            stream_data = {
            'id': stream.key.id(), 
            'name': stream.name, 
            'cover_url': stream.cover_url }
        else:
            stream_data = {
            'id': None }


        print(stream_data)

        self.response.out.write(json.dumps(stream_data))
