import webapp2
import json
import logging
from google.appengine.ext import ndb
from google.appengine.api import search
from models.stream import Stream

class DeleteStream(webapp2.RequestHandler):

    def post(self):

        json_string = self.request.body
        stream_ids = json.loads(json_string)

        for stream_id in stream_ids:
            clean_id = int(stream_id)
            stream = Stream.get_by_id(clean_id)
            stream.key.delete()

        #TODO: Delete search index as well

        self.response.out.write(json.dumps(stream_ids))