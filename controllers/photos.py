import webapp2
import logging
import sys
import datetime

from models.stream import Stream

from google.appengine.ext import blobstore
from google.appengine.ext.webapp import blobstore_handlers

class PhotoUploadHandler(blobstore_handlers.BlobstoreUploadHandler):
    def post(self):
        try:
            uploads = self.get_uploads()
            logging.info(uploads)
            if len(uploads) > 0:
                
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