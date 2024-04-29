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

            cursor.execute("SELECT score FROM grades")  # Retrieve all scores
            scores = [row[0] for row in cursor.fetchall()]

            # Calculate highest score, lowest score, total score, total number of students, and average score
            highest = max(scores)
            lowest = min(scores)
            total_score = sum(scores)
            total_students = len(scores)
            average_score = total_score / total_students if total_students > 0 else 0

            # Calculate grade distribution
            cursor.execute("SELECT grade FROM grades")
            grades = [row[0] for row in cursor.fetchall()]

            # Calculate grade distribution
            grade_counts = {'A': 0, 'B': 0, 'C': 0, 'D': 0, 'F': 0}
            for grade in grades:
                grade_counts[grade] += 1
            total_grades = sum(grade_counts.values())
            grade_distribution = {grade: count / total_grades * 100 for grade, count in grade_counts.items()}


            cursor.close()
            conn.close()

            return render_template('results.html', data=data, highest=highest, lowest=lowest,
                                   total_score=total_score, total_students=total_students,
                                   average_score=average_score, grade_distribution=grade_distribution)
        except mysql.connector.Error as err:
            print("Error:", err)
            return "An error occurred while processing your request."
        except ZeroDivisionError:
            print("Error: Division by zero")
            return "An error occurred while processing your request. Division by zero."
        except Exception as e:
            print("Error:", e)
            return "An error occurred while processing your request."
    else:
        return render_template('results.html', data=None)

if __name__ == '__main__':
    app.run(debug=True)
