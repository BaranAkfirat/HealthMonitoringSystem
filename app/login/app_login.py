from flask import Flask, request, jsonify
import pymysql

app = Flask(__name__)

# Connect to the local MySQL database
connection = pymysql.connect(
    host='localhost',
    user='root',  # Replace with your MySQL username
    password='',  # Replace with your MySQL password
    database='health_monitoring',  # Replace with your database name
    charset='utf8mb4',
    cursorclass=pymysql.cursors.DictCursor
)

@app.route('/get_profile', methods=['GET'])
def get_profile():
    try:
        username = request.args.get('username')
        if not username:
            return jsonify({'error': 'Username is required'}), 400

        with connection.cursor() as cursor:
            sql = "SELECT * FROM user WHERE username = %s"
            cursor.execute(sql, (username,))
            result = cursor.fetchone()

            if result:
                return jsonify({'id': result['id'], 'username': result['username'], 'password': result['password']})
            else:
                return jsonify({'error': 'User not found'}), 404
    except Exception as e:
        return jsonify({'error': str(e)}), 500

@app.route('/')
def index():
    return "Flask server is running!"

@app.route('/login', methods=['POST'])
def login():
    try:
        data = request.json
        username = data.get('username')
        password = data.get('password')

        print(f"Received username: {username}, password: {password}")

        if not (username and password):
            return jsonify({'error': 'Username and password are required'}), 400

        with connection.cursor() as cursor:
            sql = "SELECT * FROM user WHERE username = %s AND password = %s"
            cursor.execute(sql, (username, password))
            result = cursor.fetchone()

            if result:
                return jsonify({'message': 'Login successful'})
            else:
                return jsonify({'error': 'Invalid username or password'}), 401
    except Exception as e:
        print(f"Exception: {e}")
        return jsonify({'error': str(e)}), 500

@app.route('/add_data', methods=['POST'])
def add_data():
    try:
        data = request.json
        username = data.get('username')
        password = data.get('password')

        print(f"Received username: {username}, password: {password}")

        if not (username and password):
            return jsonify({'error': 'Username and password are required'}), 400

        with connection.cursor() as cursor:
            sql = "INSERT INTO user (username, password) VALUES (%s, %s)"
            cursor.execute(sql, (username, password))
            connection.commit()

        return jsonify({'message': 'Data added successfully'})
    except Exception as e:
        print(f"Exception: {e}")
        return jsonify({'error': str(e)}), 500

@app.route('/add_pill', methods=['POST'])
def add_pill():
    try:
        data = request.json
        pill_name = data.get('pill_name')

        if not pill_name:
            return jsonify({'error': 'Pill name is required'}), 400

        with connection.cursor() as cursor:
            sql = "INSERT INTO pills (pill_name) VALUES (%s)"
            cursor.execute(sql, (pill_name,))
            connection.commit()

        return jsonify({'message': 'Pill added successfully'})
    except Exception as e:
        print(f"Exception: {e}")
        return jsonify({'error': str(e)}), 500

@app.route('/get_pills', methods=['GET'])
def get_pills():
    try:
        with connection.cursor() as cursor:
            sql = "SELECT pill_name FROM pills"
            cursor.execute(sql)
            results = cursor.fetchall()
            if results:
                pill_names = [row['pill_name'] for row in results]
                return jsonify({'pill_names': pill_names})
            else:
                return jsonify({'error': 'No pill names found'}), 404
    except Exception as e:
        return jsonify({'error': str(e)}), 500

@app.route('/add_appointment', methods=['POST'])
def add_appointment():
    try:
        data = request.json
        appointment_name = data.get('appointment_name')

        if not appointment_name:
            return jsonify({'error': 'Appointment name is required'}), 400

        with connection.cursor() as cursor:
            sql = "INSERT INTO appointment (appointment_name) VALUES (%s)"
            cursor.execute(sql, (appointment_name,))
            connection.commit()

        return jsonify({'message': 'Appointment added successfully'})
    except Exception as e:
        print(f"Exception: {e}")
        return jsonify({'error': str(e)}), 500

@app.route('/get_appointments', methods=['GET'])
def get_appointments():
    try:
        with connection.cursor() as cursor:
            sql = "SELECT appointment_name FROM appointment"
            cursor.execute(sql)
            results = cursor.fetchall()
            if results:
                appointment_names = [row['appointment_name'] for row in results]
                return jsonify({'appointment_names': appointment_names})
            else:
                return jsonify({'error': 'No appointment names found'}), 404
    except Exception as e:
        return jsonify({'error': str(e)}), 500


@app.route('/add_heart', methods=['POST'])
def add_heart():
    try:
        data = request.json
        heart_name = data.get('heart_name')

        if not heart_name:
            return jsonify({'error': 'Heart name is required'}), 400

        with connection.cursor() as cursor:
            sql = "INSERT INTO heart (heart_name) VALUES (%s)"
            cursor.execute(sql, (heart_name,))
            connection.commit()

        return jsonify({'message': 'Heart rate added successfully'})
    except Exception as e:
        print(f"Exception: {e}")
        return jsonify({'error': str(e)}), 500

@app.route('/get_heart', methods=['GET'])
def get_heart():
    try:
        with connection.cursor() as cursor:
            sql = "SELECT heart_name FROM heart"
            cursor.execute(sql)
            results = cursor.fetchall()
            if results:
                heart_names = [row['heart_name'] for row in results]
                return jsonify({'heart_names': heart_names})
            else:
                return jsonify({'error': 'No heart rates found'}), 404
    except Exception as e:
        return jsonify({'error': str(e)}), 500

@app.route('/add_sleep', methods=['POST'])
def add_sleep():
    try:
        data = request.json
        sleep_name = data.get('sleep_name')

        if not sleep_name:
            return jsonify({'error': 'Sleep name is required'}), 400

        with connection.cursor() as cursor:
            sql = "INSERT INTO sleep (sleep_name) Values (%s)"
            cursor.execute(sql, (sleep_name,))
            connection.commit()

        return jsonify({'message': 'Sleep added successfully'})
    except Exception as e:
        print(f"Exception: {e}")
        return jsonify ({'error': str(e)}), 500

@app.route('/get_sleep', methods=['GET'])
def get_sleep():
    try:
        with connection.cursor() as cursor:
            sql = "SELECT sleep_name FROM sleep"
            cursor.execute(sql)
            results = cursor.fetchall()
            if results:
                sleep_names = [row['sleep_name'] for row in results]
                return jsonify ({'sleep_names': sleep_names})
            else:
                return jsonify ({'error': 'No sleep found'}), 404
    except Exception as e:
        return jsonify({'error': str(e)}), 500


@app.route('/add_sport', methods=['POST'])
def add_sport():
    try:
        data = request.json
        sport_name = data.get('sport_name')

        if not sport_name:
            return jsonify({'error': 'Sport name is required'}), 400

        with connection.cursor() as cursor:
            sql = "INSERT INTO sport (sport_name) VALUES (%s)"
            cursor.execute(sql, (sport_name,))
            connection.commit()

        return jsonify({'message': 'Sport added successfuly'})
    except Exception as e:
        print(f"Exception: {e}")
        return jsonify({'error': str(e)}), 500

@app.route('/get_sport', methods=['GET'])
def get_sport():
    try:
        with connection.cursor() as cursor:
            sql = "SELECT sport_name FROM sport"
            cursor.execute(sql)
            results = cursor.fetchall()
            if results:
                sport_names = [row['sport_name'] for row in results]
                return jsonify ({'sport_names': sport_names})
            else:
                return jsonify ({'error': 'No sport found'}), 404
    except Exception as e:
        return jsonify({'error': str(e)}), 500

if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5555)
