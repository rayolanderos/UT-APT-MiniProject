import webapp2
import json
import datetime
import logging


from models.stream import Stream

class Trending(webapp2.RequestHandler):

    def get(self):
        self.response.headers['Content-Type'] = 'application/json'
        user = self.request.get('user')

        streams_query = Stream.query(Stream.trending_rank>0).order(Stream.trending_rank)
        streams = streams_query.fetch()
        stream_list = []

        for stream in streams:
            stream_list.append({'id': stream.key.id(), 'name': stream.name, 'cover_url': stream.cover_url, 'views_list_count': len(stream.views_list) })


        print(stream_list)

        self.response.out.write(json.dumps(stream_list))
