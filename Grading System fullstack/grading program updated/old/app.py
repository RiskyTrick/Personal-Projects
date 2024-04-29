from flask import Flask, render_template, request, redirect, url_for
import mysql.connector
import subprocess

app = Flask(__name__)

mysql_config = {
    'host': 'localhost',
    'user': 'root',
    'password': 'skbn5359',
    'database': 'exam_database'
}

def get_mysql_connection():
    return mysql.connector.connect(**mysql_config)

def call_worker():
    subprocess.run(["python", "worker.py"])

@app.route('/', methods=['GET', 'POST'])
def index():
    if request.method == 'POST':
        first_name = request.form.get('first_name')
        last_name = request.form.get('last_name')
        student_id = request.form.get('student_id')
        exam_response = request.form.get('exam_response')

        try:
            conn = get_mysql_connection()
            cursor = conn.cursor()

            cursor.execute("INSERT INTO students (student_id, first_name, last_name, exam_response) VALUES (%s, %s, %s, %s)",
                           (student_id, first_name, last_name, exam_response))
        
            conn.commit()

            cursor.close()
            conn.close()

            
            call_worker()

            return redirect(url_for('results'))  
        except mysql.connector.Error as err:
            print("Error:", err)
            return "An error occurred while processing your request."
    else:
        return render_template('index.html')

@app.route('/results', methods=['GET', 'POST'])
def results():
    if request.method == 'POST':
        student_id = request.form.get('student_id')
        try:
            conn = get_mysql_connection()
            cursor = conn.cursor()

            cursor.execute("SELECT * FROM grades WHERE student_id = %s", (student_id,))
            data = cursor.fetchone()

            cursor.close()
            conn.close()

            return render_template('results.html', data=data)
        except mysql.connector.Error as err:
            print("Error:", err)
            return "An error occurred while processing your request."
    else:
        return render_template('results.html', data=None)

if __name__ == '__main__':
    app.run(debug=True)
