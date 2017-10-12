import webapp2
import logging
import sys
import datetime
import random
import json

from models.stream import Stream

from google.appengine.ext import blobstore
from google.appengine.api import urlfetch
from google.appengine.api import images
from google.appengine.ext.webapp import blobstore_handlers

class PhotoUploadHandler(blobstore_handlers.BlobstoreUploadHandler):
    def post(self):
        try:
            uploads = self.get_uploads()
            logging.info(uploads)
            if len(uploads) > 0:
<<<<<<< HEAD
                
=======

                #Associate with stream
                upload = uploads[0]
>>>>>>> master
                stream_id = int(self.request.get('stream_id'))
                stream = Stream.get_by_id(stream_id)
                logging.info('Stream ID: {}, Photos: {}'.format(stream_id, len(stream.photos)))

                for upload in uploads:
                    logging.info(upload)
                    logging.info(upload.key())
                    stream.photos.insert(0, upload.key())

                logging.info('[After] Stream ID: {}, Photos: {}'.format(stream_id, len(stream.photos)))
                stream.date = datetime.datetime.now()
                stream.put()

                #Store in datastore
                photo_data = { 
                    'url': images.get_serving_url(upload.key()),
                    'stream_id': stream_id, 
                    'lat': random.uniform(90, -90), 
                    'lon': random.uniform(180, -180)
                }
        
                create_photo_api = self.uri_for('api-create-photo', _full=True)

                result = urlfetch.fetch(
                    url=create_photo_api,
                    payload=json.dumps(photo_data),
                    method=urlfetch.POST,
                    headers= {'Content-Type': 'application/json'}
                )

            else:
                # TODO display an error message
                pass

            view_uri = '{}?id={}'.format(self.uri_for('view'), stream_id)
            self.redirect(view_uri)

        except:
            e = sys.exc_info()[0]
            logging.exception(e)
            self.error(500)

class GenerateUploadUrlHandler(blobstore_handlers.BlobstoreUploadHandler):
    def get(self):
        self.response.headers['Content-Type'] = 'text/plain'
        self.response.out.write(blobstore.create_upload_url('/upload_photo'))