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
        owner = dict_object['owner']

        same_name = Stream.query(Stream.name == stream_name).fetch()

        if not same_name:
            stream = Stream(name=stream_name, cover_url=stream_cover_url, tags=tags, photos=[], owner=owner, views=0, views_list=[])
            stream_key = stream.put()
            stream_url = '/view?id={0}'.format(stream_key.id() )
            res = { "msg" : "Stream Created", "success": True, "stream_url" : stream_url }
            self.response.out.write(json.dumps(res))
            # self.response.location = '/view?id={0}'.format(stream_key.id())
        else:
            res = { "msg" : "You tried to create a stream whose name is the same as an existing stream. The operation did not complete.", "success": False }
            self.response.out.write(json.dumps(res))