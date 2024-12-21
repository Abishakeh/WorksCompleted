import sqlite3
from flask import Flask, render_template,request, url_for, flash, redirect, session
from werkzeug.exceptions import abort

def get_db_connection():
    conn = sqlite3.connect('database.db')
    conn.row_factory = sqlite3.Row
    return conn

def get_post(post_id):
    conn = get_db_connection()
    post = conn.execute('SELECT * FROM posts WHERE id = ?',
                    (post_id,)).fetchone()
    conn.close()
    if post is None:
        abort(404)
    return post

def get_comment(post_id):
    conn = get_db_connection()
    comments = conn.execute('SELECT * FROM comments WHERE post_id = ?',
                    (post_id,)).fetchall()
    conn.close()
    return comments

def get_comment_edit(comment_id):
    conn = get_db_connection()
    comment = conn.execute('SELECT * FROM comments WHERE id = ?',
                    (comment_id,)).fetchone()
    conn.close()
    return comment

def check_userName(username):
    conn = get_db_connection()
    user = conn.execute('SELECT * FROM users WHERE userName = ?', (username,)).fetchone()
    conn.close()

    return user is None

app = Flask(__name__)
app.config['SECRET_KEY'] = 'your secret key'

@app.route('/')
def home():
    return render_template('home.html')

@app.route('/login', methods=('GET','POST'))
def login():
    if request.method == 'POST':
        username = request.form['userName']
        password = request.form['password']
        if not username and not password:
            flash('Username and Password is required')
        elif not username:
            flash('Username is required')
        elif not password:
            flash('Password is required')
        else:
            conn = get_db_connection()
            user = conn.execute('SELECT * FROM users WHERE userName =? AND password = ?',
                         (username,password)).fetchone()
            conn.close()
            if user:
                    storedUsername = user['userName']
                    storedPassword = user['password']
                    if username == storedUsername and password == storedPassword:
                        session['user_id'] = user['id']
                        flash('Login successful!')
                        return redirect(url_for('index')) 
                    else:
                        flash('Login unsuccessful')
            else:
                flash('Login unsuccessful')
    return render_template('login.html')


@app.route('/register', methods=('GET', 'POST'))
def register():
    if request.method == 'POST':
        username = request.form.get('userName')
        password = request.form.get('password')
        if not username or not password:
            flash('Username and Password are required')
        elif not username:
            flash('The username is required')
        elif not password:
            flash('The password is required')
        elif check_userName(username) == False:
            flash('Enter another username because this username is already taken')
            print("Username already exists!")
            return redirect(url_for('register'))
        else:
            conn = get_db_connection()
            conn.execute('INSERT INTO users (userName, password) VALUES (?, ?)', (username, password))
            conn.commit()
            conn.close()
            conn = get_db_connection()
            user = conn.execute('SELECT * FROM users WHERE userName =? AND password = ?',
                         (username,password)).fetchone()
            conn.close()
            session['user_id'] = user['id']
            flash('Registration successful!')
            return redirect(url_for('index'))

    return render_template('register.html')


@app.route('/blog')
def index():
    conn = get_db_connection()
    posts = conn.execute('SELECT * FROM posts').fetchall()
    user_id = session['user_id']
    conn.close()
    return render_template('index.html', user_id=user_id ,posts=posts)

@app.route('/<int:post_id>')
def post(post_id):
    post = get_post(post_id)
    return render_template('post.html',post = post)

@app.route('/<int:id>/edit', methods=('GET','POST'))
def edit(id):
    post = get_post(id)
    if request.method == 'POST':
        title = request.form['title']
        content = request.form['content']

        if not title:
            flash('Title is required!')
        else:
            conn = get_db_connection()
            conn.execute('UPDATE posts SET title = ?, content = ?'
                         'WHERE id = ?',
                         (title,content,id))
            conn.commit()
            conn.close()
            return redirect(url_for('index'))
    return render_template('edit.html', post=post)

@app.route('/create', methods=('GET','POST'))
def create():
    if request.method == 'POST':
        title = request.form['title']
        content = request.form['content']
        user_id = session['user_id']


        if not title and not content:
            flash('The title and content is required')
        elif not title:
            flash('The title is required!')
        elif not content:
            flash('The content is required!')
        else:
            conn = get_db_connection()
            conn.execute('INSERT INTO posts (title, content, user_id) VALUES (?,?,?)',
                            (title,content,user_id))
            conn.commit()
            conn.close()
            return redirect(url_for('index'))
    return render_template('create.html')

@app.route('/recreate')
def recreate():
    conn = get_db_connection()

    conn.execute('DROP TABLE IF EXISTS users')
    conn.execute('DROP TABLE IF EXISTS posts')
    conn.execute('DROP TABLE IF EXISTS comments')
    
    conn.execute('''
        CREATE TABLE users(
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            userName TEXT NOT NULL,
            password TEXT NOT NULL
        )
''')
    
    conn.execute('''
        CREATE TABLE posts (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            created TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
            title TEXT NOT NULL,
            content TEXT NOT NULL,
            user_id INTEGER NOT NULL,
            FOREIGN KEY (user_id) REFERENCES users (id)
        )
''')
    
    conn.execute('''
        CREATE TABLE comments(
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                comment_Title TEXT NOT NULL,
                comment_Description TEXT NOT NULL,
                user_id INTEGER NOT NULL,
                post_id INTEGER NOT NULL,
                FOREIGN KEY (user_id) REFERENCES users (id),
                FOREIGN KEY (post_id) REFERENCES users (id)
                )
        ''')
    
    conn.commit()
    conn.close()

    return render_template('create.html')
    
@app.route('/recreate_tables')
def recreate_tables():
    conn = get_db_connection()

    # Drop tables if they exist
    conn.execute('DROP TABLE IF EXISTS users')
    conn.execute('DROP TABLE IF EXISTS posts')
    conn.execute('DROP TABLE IF EXISTS comments')

    # Recreate users table
    conn.execute('''
        CREATE TABLE users (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            userName TEXT NOT NULL UNIQUE,
            password TEXT NOT NULL
        )
    ''')

    # Recreate posts table
    conn.execute('''
        CREATE TABLE posts (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            title TEXT NOT NULL,
            content TEXT NOT NULL,
            user_id INTEGER NOT NULL,
            FOREIGN KEY (user_id) REFERENCES users (id)
        )
    ''')


    conn.execute('''
        CREATE TABLE comments (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            comment_Title TEXT NOT NULL,
            comment_Description TEXT NOT NULL,
            user_id INTEGER NOT NULL,
            post_id INTEGER NOT NULL,
            FOREIGN KEY (post_id) REFERENCES posts (id),
            FOREIGN KEY (user_id) REFERENCES users (id)
        )
    ''')

    conn.commit()
    conn.close()

    return render_template('create.html')

@app.route('/<int:id>/comments', methods=('GET','POST'))
def comments(id):
    comments = get_comment(id)
    user_id = session['user_id']
    return render_template('comments.html',comments = comments, user_id = user_id)

@app.route('/<int:id>/createComment', methods=('GET','POST'))
def createComments(id):
    if request.method == 'POST':
        comment_title = request.form['comment_Title']
        comment_description = request.form['comment_Description']
        user_id = session['user_id']

        if not comment_title and not comment_description:
            flash('The comment title and description are required!')
        elif not comment_title:
            flash('The comment title is required!')
        elif not comment_description:
            flash('The comment description is required!')
        else:
            conn = get_db_connection()
            conn.execute('INSERT INTO comments (comment_Title, comment_Description, user_id, post_id) VALUES (?,?,?,?)',
                         (comment_title,comment_description,user_id,id))
            conn.commit()
            conn.close()
            return redirect(url_for('comments', id = id))
    return render_template('createComment.html')

@app.route('/<int:id>/delete', methods=('POST',))
def delete(id):
    post = get_post(id)
    conn = get_db_connection()
    conn.execute('DELETE FROM posts WHERE id = ?',(id,))
    conn.commit()
    conn.close()
    flash('"{}" was sucessfully deleted!'.format(post['title']))
    return redirect(url_for('index'))

@app.route('/<int:id>/<int:comment_id>/deleteComment', methods=('POST',))
def deleteComment(comment_id,id):
    conn = get_db_connection()
    conn.execute('DELETE FROM comments WHERE id =? AND post_id =?',(comment_id,id))
    conn.commit()
    conn.close()
    flash('The comment was sucessfully deleted!')
    comments = get_comment(id)
    return render_template('comments.html',comments = comments)

@app.route('/<int:id>/editComment', methods=('GET','POST'))
def editComment(id):
    comment = get_comment_edit(id)
    if request.method == 'POST':
        title = request.form['comment_Title']
        description = request.form['comment_Description']

        if not title:
            flash('Title is required!')
        else:
            conn = get_db_connection()
            conn.execute('UPDATE comments SET comment_Title = ?, comment_Description = ?'
                         'WHERE id = ?',
                         (title,description,id))
            conn.commit()
            conn.close()
            return redirect(url_for('comments',id = comment['post_id']))
    return render_template('editComment.html', comment=comment)