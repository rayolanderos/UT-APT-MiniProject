import webapp2
import json
import logging

from models.connexus_user import ConnexusUser

class CreateUser(webapp2.RequestHandler):

    def post(self):

        json_string = self.request.body
        dict_object = json.loads(json_string)

        user_id = dict_object['user_id']

        user_result = ConnexusUser.query(ConnexusUser.user_id == user_id).fetch()
        
        if not user_result: 
            user = ConnexusUser(user_id=user_id, report=0)
            user_key = user.put()

        self.response.out.write(json.dumps(dict_object))