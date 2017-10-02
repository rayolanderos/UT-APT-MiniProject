import webapp2
import json

from models.stream import Stream

class Search(webapp2.RequestHandler):

    def get(self):
        self.response.headers['Content-Type'] = 'application/json'
        streams_query = Stream.query()
        streams = streams_query.fetch()
        stream_list = []


        search = input-search.fetch() # not sure how to grab the contents of the search box

        for stream in streams:
            if search in stream.name or search in stream.tags or search in stream.key.id():
                stream_list.append({'id': stream.key.id(), 'name': stream.name, 'cover_url': stream.cover_url, 'tags': stream.tags})


        print(stream_list)

        self.response.out.write(json.dumps(stream_list))