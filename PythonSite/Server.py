import BaseHTTPServer
from src.controllers.LoginController import LoginController
from src.controllers.TasksController import TasksController

IP = "0.0.0.0"
PORT = 80

controllers = {
    "/login" : LoginController(),
    "/tasks" : TasksController()
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
    
def getFile(path, handler):
    try:
        extension = path[path.find(".") + 1: len(path)]
        path = extension + path
    except:
        pass
    try:
        f = open(path, "r")
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

Server = BaseHTTPServer.HTTPServer
server = Server((IP, PORT), Handler)

try:
    server.serve_forever()
except:
    print "Server Ended"