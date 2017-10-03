import webapp2
import json
import datetime
import logging


from models.stream import Stream

class GenerateTrending(webapp2.RequestHandler):

    def get(self):
        self.response.headers['Content-Type'] = 'application/json'

        streams_query = Stream.query()
        streams = streams_query.fetch()
        stream_list = []

        for stream in streams:
            stream.trending_rank = 0
            for view in stream.views_list:
                if view < datetime.datetime.now()-datetime.timedelta(hours=1):
                    stream.views_list.remove(view)
            stream.views_list_size = len(stream.views_list)
            stream.put()

        ordered_query = Stream.query().order(-Stream.views_list_size)
        ordered_streams = ordered_query.fetch()

        ordered_streams[0].trending_rank = 1
        ordered_streams[0].put()
        ordered_streams[1].trending_rank = 2
        ordered_streams[1].put()
        ordered_streams[2].trending_rank = 3
        ordered_streams[2].put()
        
        print(stream_list)

        self.response.out.write(json.dumps(stream_list))
