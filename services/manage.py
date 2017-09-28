import webapp2
import json

from models.stream import Stream

class Manage(webapp2.RequestHandler):

    def get(self):
        self.response.headers['Content-Type'] = 'application/json'
        streams_query = Stream.query()
        streams = streams_query.fetch()
        stream_list = []

        for stream in streams:
            stream_list.append({
            'id': stream.key.id(), 
            'name': stream.name, 
            'cover_url': stream.cover_url
            })


        print(stream_list)

        self.response.out.write(json.dumps(stream_list))
