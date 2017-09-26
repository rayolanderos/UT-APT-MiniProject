import webapp2
import json

from models.stream import Stream



class CreateStream(webapp2.RequestHandler):

    def post(self):
        stream_name = self.request.get('create-stream-name')
        stream_cover_url = self.request.get('create-stream-cover')

        stream = Stream(name=stream_name, cover_url=stream_cover_url)
        stream.put()