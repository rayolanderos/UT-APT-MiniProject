import webapp2
import json
import logging

from models.connexus_user import ConnexusUser

class UpdateUserReport(webapp2.RequestHandler):

    def post(self):

        json_string = self.request.body
        dict_object = json.loads(json_string)

        user_id = dict_object['user_id']
        report_rate = int( dict_object['report_rate'] )
        
        user_result = ConnexusUser.query(ConnexusUser.user_id == user_id).fetch()

        for user in user_result:
            user.report = report_rate
            user.put()
        
        
        res = { "msg" : "user updated", "success": True }

        self.response.out.write(json.dumps(res))

        