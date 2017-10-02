import webapp2
import json
import datetime
import logging


from models.stream import Stream

class Manage(webapp2.RequestHandler):

    def get(self):
        self.response.headers['Content-Type'] = 'application/json'
        user = self.request.get('user')
        stream_type = self.request.get('type')
        if stream_type == 'own' :
            streams = Stream.query(Stream.owner == user)
        else:
            #TODO : handle suscription views
            streams = [] 
        
        stream_list = []

        if streams is not None:
            for stream in streams:
                date = stream.date
                date_string = date.strftime('%m/%d/%Y')
                stream_list.append({
                'id': stream.key.id(), 
                'name': stream.name, 
                'date': date_string, 
                'photo_count': len(stream.photos)
                })


        print(stream_list)

        self.response.out.write(json.dumps(stream_list))
