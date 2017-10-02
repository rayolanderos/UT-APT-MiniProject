import webapp2
import json
import logging

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

        stream = Stream(name=stream_name, cover_url=stream_cover_url, tags=tags, photos=[])
        stream_key = stream.put()

        self.response.location = '/view?id={0}'.format(stream_key.id())