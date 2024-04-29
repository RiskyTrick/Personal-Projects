import mysql.connector
import statistics

def grade_exam(key, student_answers):
    score = sum(1 for k, s in zip(key, student_answers) if k == s)
    return score

def get_grade(score):
    if score >= 19:
        return 'A'
    elif score >= 17:
        return 'B'
    elif score >= 16:
        return 'C'
    elif score >= 15:
        return 'D'
    else:
        return 'F'

def process_exam_results():
    try:
        # Connect to MySQL database
        conn = mysql.connector.connect(host='localhost', user='root', password='skbn5359', database='exam_database')
        cursor = conn.cursor()

        # Fetch student details from students table
        cursor.execute("SELECT student_id, exam_response FROM students")
        student_data = cursor.fetchall()

        # Read answer key
        with open("key_file.txt", "r") as key_file:
            key = key_file.readline().strip()

        # Process grades for each student
        for student_id, exam_response in student_data:
            # Skip invalid answers
            if len(exam_response) != 20 or not exam_response.isdigit():
                print(f"Invalid answer for student_id {student_id}: {exam_response}")
                continue

            score = grade_exam(key, exam_response)
            grade = get_grade(score)

            # Check if the student already has a grade in the grades table
            cursor.execute("SELECT * FROM grades WHERE student_id = %s", (student_id,))
            existing_grade = cursor.fetchone()

            if existing_grade:
                # Update the existing grade
                cursor.execute("UPDATE grades SET score = %s, grade = %s WHERE student_id = %s", (score, grade, student_id))
            else:
                # Insert a new grade
                cursor.execute("INSERT INTO grades (student_id, score, grade) VALUES (%s, %s, %s)", (student_id, score, grade))

            conn.commit()

        # Close cursor and connection
        cursor.close()
        conn.close()

        print("Grades processing completed successfully.")
    except mysql.connector.Error as err:
        print("Error:", err)

if __name__ == "__main__":
    process_exam_results()
