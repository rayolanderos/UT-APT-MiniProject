import webapp2
import json
import logging

from models.connexus_user import ConnexusUser

class CreateUser(webapp2.RequestHandler):

    def post(self):

        json_string = self.request.body
        dict_object = json.loads(json_string)

        user_id = dict_object['user_id']

        user_result = ConnexusUser.from_user_id(user_id)
        
        if not user_result: 
            user = ConnexusUser(user_id=user_id, report=0, streams_subscribed=[])
            user_key = user.put()
