import BaseHTTPServer
from src.controllers.LoginController import LoginController
from src.controllers.TasksController import TasksController
from src.controllers.LogoutController import LogoutController

IP = "0.0.0.0"
PORT = 80

controllers = {
    "/login" : LoginController(),
    "/tasks" : TasksController(),
    "/logout" : LogoutController()
}

def getResponse(path, method, handler):
    controller = None
    try:
        controller = controllers[path]
    except:
        getFile(path, handler)
        return
    if method == "GET":
        controller.onGET(handler)
    elif method == "POST":
        controller.onPOST(handler)
    elif method == "PUT":
        controller.onPUT(handler)
    elif method == "DELETE":
        controller.onDELETE(handler)
    
def getFile(path, handler):
    if path.find("fonts") == -1:  
        try:
            extension = path[path.rfind(".") + 1: len(path)]
            path = extension + path
        except:
            pass
    else:
    	path = path[1:len(path)]
    try:
        f = open(path, "rb")
        handler.wfile.write(f.read())
    except:
        handler.wfile.write("Could not read file at " + path + "\n")

class Handler(BaseHTTPServer.BaseHTTPRequestHandler):
    
    def do_HEAD(self):
        self.send_response(200)
        self.end_header();

    def do_GET(self):
        self.send_response(200)
        self.end_headers()
        path = self.path
        if path == "/quit":
            server.server_close()
            return
        try:
            getResponse(self.path, "GET", self)
        except Exception as e:
            print e
            self.wfile.write("Page does not exist at path: " + self.path)
    
    def do_POST(self):
        self.send_response(200)
        self.end_headers()
        try:
            if self.path[len(self.path) - 1] == "?":
                self.path = self.path[0: len(self.path) - 1]
            getResponse(self.path, "POST", self)
        except Exception as e:
            print e
    
    def do_PUT(self):
        self.send_response(200)
        self.end_headers()
        try:
            getResponse(self.path, "PUT", self)
        except Exception as e:
            print e
    
    def do_DELETE(self):
        self.send_response(200)
        self.end_headers()
        try:
            getResponse(self.path, "DELETE", self)
        except Exception as e:
            print e

Server = BaseHTTPServer.HTTPServer
server = Server((IP, PORT), Handler)

try:
    server.serve_forever()
except:
    print "Server Ended"