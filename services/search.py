import webapp2
import json

from google.appengine.api import search

from models.stream import Stream

class Search(webapp2.RequestHandler):

    def get(self):
        self.response.headers['Content-Type'] = 'application/json'
        streams_query = Stream.query()
        streams = streams_query.fetch()
        stream_list = []


        # search = input-search.fetch() # not sure how to grab the contents of the search box

        # for stream in streams:
        #     if search in stream.name or search in stream.tags or search in stream.key.id():
        #         stream_list.append({'id': stream.key.id(), 'name': stream.name, 'cover_url': stream.cover_url, 'tags': stream.tags})

        query = input-search.fetch() # not sure how to grab the contents of the search box

        try:
            index = search.Index('streams', namespace='namespace') # temporary as don't know current namespace if created
            search_results = index.search(query)
            returned_count = len(search_results.results)
            number_found = search_results.number_found
            for doc in search_results:
                doc_id = doc.doc_id
                fields = doc.fields
        except search.Error:
            doc_id = None
            fields = None
            number_found = 0

        # ...

        for stream_field in fields:
            stream_list.append(stream_field)


        print(stream_list)

        self.response.out.write(json.dumps(stream_list))