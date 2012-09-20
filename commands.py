# Lint

import getopt
from play.utils import *

MODULE = "lint"

COMMANDS = ["lint:", "lint:ov", "lint:override"]

HELP = {
    "lint:": "Show help for the lint module",
    "lint:override": "Override the stylesheet LESS, login or layout"
}

def execute(**kargs):
    command = kargs.get("command")
    app = kargs.get("app")
    args = kargs.get("args")
    env = kargs.get("env")

    if command == 'lint:':
        print "~ Use: --less to override the Lint less" 
        print "~      --login to override the login page" 
        print "~      --layout to override the login layout page" 
        print "~ "
        return

    try:
        optlist, args2 = getopt.getopt(args, '', ['less', 'login', 'layout'])
        for o, a in optlist:
            if o == '--less':
                app.override('public/stylesheets/lint.less', 'public/stylesheets/lint.less')
                print "~ "
                return
            if o == '--login':
                app.override('app/views/Lint/login.html', 'app/views/Lint/login.html')
                print "~ "
                return
            if o == '--layout':
                app.override('app/views/Lint/layout.html', 'app/views/Lint/layout.html')
                print "~ "
                return

    except getopt.GetoptError, err:
        print "~ %s" % str(err)
        print "~ "
        sys.exit(-1)
