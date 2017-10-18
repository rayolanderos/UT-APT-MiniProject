import webapp2
import json
import datetime
import logging


from models.stream import Stream
from models.connexus_user import ConnexusUser

class Manage(webapp2.RequestHandler):

    def get(self):
        self.response.headers['Content-Type'] = 'application/json'
        user_id = self.request.get('user')
        stream_type = self.request.get('type')
        if stream_type == 'own' :
            streams = Stream.query(Stream.owner == user_id)
        else:
            user = ConnexusUser.from_user_id(user_id)
            streams = user.get_subscribed_streams()

        stream_list = []

        if streams is not None:
            for stream in streams:
                if stream is not None:
                    date = stream.date
                    date_string = date.strftime('%m/%d/%Y')
                    stream_list.append({
                    'id': stream.key.id(), 
                    'name': stream.name, 
                    'date': date_string, 
                    'photo_count': len(stream.photos),
                    'all_time_views': stream.views
                    })

        self.response.out.write(json.dumps(stream_list))
