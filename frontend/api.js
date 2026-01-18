const API_URL = 'http://localhost:8080';

async function loadClinics(onChangeCallback = null) {
    const selects = document.getElementsByClassName('clinicSelect');

    if (selects.length === 0) return;
    try {
        const res = await fetch(API_URL + '/clinics');
        const clinics = await res.json();

        Array.from(selects).forEach(select => {
            select.innerHTML = '<option value="">Select Clinic</option>';

            clinics.forEach(c => {
                select.innerHTML += `
                    <option value="${c.id}">${c.name}</option>
                `;
            });

            if (onChangeCallback) {
                select.onchange = () => onChangeCallback(select.value);
            }
        });

        console.log('Clinics loaded to dropdown');
    } catch (err) {
        console.error('Failed to load clinics:', err);
    }
}


if (
    window.location.pathname.includes('index.html') ||
    window.location.pathname === '/'
) {
    document.addEventListener('DOMContentLoaded', () => {
        loadClinicTable();
        loadClinics();
    });

    async function loadClinicTable() {
        try {
            const res = await fetch(API_URL + '/clinics');
            const clinics = await res.json();

            const table = document.getElementById('clinicList');
            if (!table) return;

            table.innerHTML = clinics.length === 0
                ? '<tr><td colspan="5">No clinics</td></tr>'
                : clinics.map(c => `
                    <tr>
                        <td>${c.id}</td>
                        <td>${c.name}</td>
                        <td>${c.address}</td>
                        <td>${c.phoneNumber}</td>
                        <td>
                            <button onclick="deleteClinic(${c.id})">Delete</button>
                        </td>
                    </tr>
                `).join('');
        } catch (e) {
            console.error('Failed to load clinics', e);
        }
    }

    window.deleteClinic = async function (id) {
        if (!confirm('Do you want to delete?')) return;
        await fetch(`${API_URL}/clinics/${id}`, { method: 'DELETE' });
        loadClinicTable();
        loadClinics();
    };

    window.addClinic = async function () {
        const name = document.getElementById('clinicName').value.trim();
        if (!name) {
            alert('Clinic name cannot be empty');
            return;
        }

        const clinic = {
            name,
            address: document.getElementById('clinicAddress')?.value || '',
            phoneNumber: document.getElementById('clinicContact')?.value || ''
        };

        await fetch(API_URL + '/clinics/save', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(clinic)
        });

        loadClinicTable();
        loadClinics();
    };
}
if (window.location.pathname.includes('doctors.html')) {
    document.addEventListener('DOMContentLoaded', async () => {
       
        await loadClinics();
    
        const filterSelect = document.querySelector('.filter .clinicSelect');
        if (filterSelect) {
            try {
                const res = await fetch(API_URL + '/clinics');
                const clinics = await res.json();
                
                filterSelect.innerHTML = '<option value="">All Clinics</option>';
                clinics.forEach(c => {
                    filterSelect.innerHTML += `<option value="${c.id}">${c.name}</option>`;
                });
                
                filterSelect.addEventListener('change', (e) => {
                    const clinicId = e.target.value || null;
                    selectedClinicId = clinicId;
                    loadDoctorsByClinic(clinicId);
                });
            } catch (err) {
                console.error('Failed to load filter clinics:', err);
            }
        }
        
        loadAllDoctors();
    });
}

async function loadAllDoctors() {
    try {
        const res = await fetch(`${API_URL}/doctors`);
        const doctors = await res.json();
        renderDoctors(doctors);
    } catch (e) {
        console.error('Failed to load doctors', e);
    }
}

let selectedClinicId = null;

async function loadDoctorsByClinic(clinicId) {
    selectedClinicId = clinicId || null;

    if (!clinicId) {
        loadAllDoctors();
        return;
    }

    try {
        const res = await fetch(`${API_URL}/clinics/${clinicId}/doctors`);
        const doctors = await res.json();
        renderDoctors(doctors);
    } catch (e) {
        console.error('Failed to load clinic doctors', e);
    }
}

function renderDoctors(doctors) {
    const tbody = document.getElementById('doctorTable');
    if (!tbody) return;

    tbody.innerHTML = doctors.length === 0
        ? `<tr><td colspan="3">No doctors</td></tr>`
        : doctors.map(d => `
            <tr>
                <td>${d.name}</td>
                <td>${d.surname}</td>
                <td>
                    <button onclick="deleteDoctor(${d.id})">Delete</button>
                </td>
            </tr>
        `).join('');
}


window.addDoctorToClinic = async function () {
    const name = document.getElementById('docName').value.trim();
    const surname = document.getElementById('docSurname').value.trim();
    const formSelect = document.querySelector('.form .clinicSelect');
    const clinicId = formSelect?.value;

    if (!name || !surname || !clinicId) {
        alert('Fill all fields');
        return;
    }

    try {
        const saveRes = await fetch(`${API_URL}/doctors/save`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ name, surname })
        });

        if (!saveRes.ok) {
            const errorText = await saveRes.text();
            throw new Error(`Failed to save doctor: ${errorText}`);
        }

        const allDoctorsRes = await fetch(`${API_URL}/doctors`);
        if (!allDoctorsRes.ok) {
            throw new Error('Failed to get doctors list');
        }
        
        const doctors = await allDoctorsRes.json();
        const createdDoctor = doctors.find(d => d.name === name && d.surname === surname);

        if (!createdDoctor || !createdDoctor.id) {
            alert('Doctor saved but not found. Please refresh the page.');
            loadAllDoctors();
            return;
        }

        const assignRes = await fetch(
            `${API_URL}/clinics/${clinicId}/assignDoctor/${createdDoctor.id}`,
            { method: 'GET' }  
        );

        if (!assignRes.ok) {
            const errorText = await assignRes.text();
            console.error('Assignment error:', errorText);
            throw new Error(`Failed to assign doctor to clinic: ${errorText}`);
        }

        
        selectedClinicId = clinicId;
        
 
        const filterSelect = document.querySelector('.filter .clinicSelect');
        if (filterSelect) {
            filterSelect.value = clinicId;
        }
        
        
        loadDoctorsByClinic(clinicId);

        document.getElementById('docName').value = '';
        document.getElementById('docSurname').value = '';

    } catch (e) {
        console.error('Doctor addition error:', e);
        alert(`Error occurred while adding doctor: ${e.message || e}`);
    }
};


window.deleteDoctor = async function (doctorId) {
    if (!confirm('Delete doctor?')) return;

    try {

        const filterSelect = document.querySelector('.filter .clinicSelect');
        const clinicId = filterSelect?.value || null;

        
        if (clinicId) {
            const removeRes = await fetch(`${API_URL}/clinics/${clinicId}/removeDoctor/${doctorId}`, {
                method: 'DELETE'
            });

            const removeText = await removeRes.text().catch(() => '');

            if (!removeRes.ok) {
                console.error('Error removing doctor from clinic:', removeText, 'Status:', removeRes.status);
                return;
            }

            
            selectedClinicId = clinicId;
            await loadDoctorsByClinic(clinicId);
            return;
        }

      
        const res = await fetch(`${API_URL}/doctors/${doctorId}`, {
            method: 'DELETE'
        });

        const text = await res.text().catch(() => '');

        if (!res.ok) {
            console.error('Doctor deletion error (backend):', text, 'Status:', res.status);
            alert(`Failed to delete doctor. Backend error: ${text || res.status}`);
            return;
        }

      
        selectedClinicId = null;
        await loadAllDoctors();

    } catch (e) {
        console.error('Failed to delete doctor', e);
    }
};


if (window.location.pathname.includes('patient.html')) {
    document.addEventListener('DOMContentLoaded', () => {
     
        const ageSelect = document.getElementById('patientAge');
        if (ageSelect) {
            ageSelect.innerHTML = '<option value="">Select Age</option>';
            for (let i = 1; i <= 100; i++) {
                ageSelect.innerHTML += `<option value="${i}">${i}</option>`;
            }
        }
        
       
        loadClinics();
        
       
        const filterClinicSelect = document.querySelector('.filter-box .clinicSelect');
        if (filterClinicSelect) {
            filterClinicSelect.addEventListener('change', (e) => {
                const clinicId = e.target.value;
                loadDoctorsForClinic(clinicId);
            });
        }
        
        
        const formClinicSelect = document.querySelector('.form .clinicSelect');
        if (formClinicSelect) {
           
            formClinicSelect.addEventListener('change', (e) => {
                const clinicId = e.target.value;
                loadDoctorsForClinicForm(clinicId);
            });
        }
    });

    
    async function loadDoctorsForClinicForm(clinicId) {
        if (!clinicId) {
            const formDoctorSelect = document.querySelector('.form .doctorSelect');
            if (formDoctorSelect) {
                formDoctorSelect.innerHTML = '<option value="">Select Doctor</option>';
            }
            return;
        }

        try {
            const res = await fetch(`${API_URL}/clinics/${clinicId}/doctors`);
            const doctors = await res.json();

            const formDoctorSelect = document.querySelector('.form .doctorSelect');
            if (formDoctorSelect) {
                formDoctorSelect.innerHTML = '<option value="">Select Doctor</option>';
                doctors.forEach(d => {
                    formDoctorSelect.innerHTML += `<option value="${d.id}">${d.name} ${d.surname}</option>`;
                });
            }
        } catch (e) {
            console.error('Failed to load form doctors', e);
        }
    }

        
    async function loadDoctorsForClinic(clinicId) {
        if (!clinicId) {
            
            const select = document.querySelector('.filter-box .doctorSelect');
            if (select) {
                select.innerHTML = '<option value="">Select Doctor</option>';
            }
            return;
        }

        try {
            const res = await fetch(`${API_URL}/clinics/${clinicId}/doctors`);
            const doctors = await res.json();

        
            const select = document.querySelector('.filter-box .doctorSelect');
            if (!select) {
                console.error('Filter doctor select element not found');
                return;
            }

            select.innerHTML = '<option value="">Select Doctor</option>';
            doctors.forEach(d => {
                select.innerHTML += `<option value="${d.id}">${d.name} ${d.surname}</option>`;
            });

            
            select.onchange = () => loadPatientsForDoctor(select.value);
        } catch (e) {
            console.error('Failed to load doctors', e);
        }
    }

    async function loadPatientsForDoctor(doctorId) {
        if (!doctorId) {
            
            const table = document.getElementById('patientTable');
            if (table) {
                table.innerHTML = '';
            }
            return;
        }

        try {
            const res = await fetch(`${API_URL}/doctors/${doctorId}/patients`);
            const patients = await res.json();

            const table = document.getElementById('patientTable');
            if (!table) {
                console.error('Patient table element not found');
                return;
            }

            table.innerHTML = patients.length === 0
                ? '<tr><td colspan="6">No patients</td></tr>'
                : patients.map(p => `
                    <tr>
                        <td>${p.id}</td>
                        <td>${p.name}</td>
                        <td>${p.surname}</td>
                        <td>${p.age}</td>
                        <td>${p.TC}</td>
                        <td>${p.phoneNumber}</td>
                    </tr>
                `).join('');
        } catch (e) {
            console.error('Failed to load patients', e);
            alert('Error occurred while loading patients');
        }
    }

    window.addPatientToClinicAndDoctor = async function () {
        const name = document.getElementById('patientName').value.trim();
        const surname = document.getElementById('patientSurname').value.trim();
        const ageSelect = document.getElementById('patientAge');
        const age = ageSelect ? ageSelect.value : '';
        const TC = document.getElementById('patientTC').value.trim();
        const phoneNumber = document.getElementById('patientPhone').value.trim();
        
        console.log('Form values:', { name, surname, age, TC, phoneNumber });
        
    
        const formClinicSelect = document.querySelector('.form .clinicSelect');
        const formDoctorSelect = document.querySelector('.form .doctorSelect');
        const clinicId = formClinicSelect?.value;
        const doctorId = formDoctorSelect?.value;
        
        console.log('Select values:', { clinicId, doctorId });

        if (!name || !surname || !age || !TC || !clinicId || !doctorId) {
            console.error('Missing fields:', { name: !name, surname: !surname, age: !age, TC: !TC, clinicId: !clinicId, doctorId: !doctorId });
            alert('Fill all fields');
            return;
        }

        try {
            const ageNum = parseInt(age);
            console.log('Age:', ageNum);
            if (isNaN(ageNum) || ageNum <= 0) {
                alert('Please select a valid age');
                return;
            }

            
            const patientData = { 
                name, 
                surname, 
                age: ageNum, 
                TC, 
                phoneNumber 
            };
            
            let saveRes;
            try {
                saveRes = await fetch(`${API_URL}/patients/save`, {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify(patientData)
                });
                
                console.log('Response status:', saveRes.status);
                console.log('Response ok:', saveRes.ok);
            } catch (err) {
                throw new Error(`Backend connection error: ${err.message}. Endpoint: ${API_URL}/patients/save`);
            }

            if (!saveRes.ok) {
                const errorText = await saveRes.text();
                throw new Error(`Failed to save patient: ${errorText}`);
            }

            let allPatientsRes;
            try {
                allPatientsRes = await fetch(`${API_URL}/patients`);
            } catch (err) {
                console.error('Fetch error details:', err);
                throw new Error(`Backend connection error: ${err.message}. Backend server (${API_URL}) is running and CORS settings are correct.`);
            }
            
            if (!allPatientsRes.ok) {
                throw new Error('Failed to get patients list');
            }
            
            const patients = await allPatientsRes.json();
            const createdPatient = patients.find(p => 
                p.name === name && 
                p.surname === surname && 
                p.TC === TC
            );

            if (!createdPatient || !createdPatient.id) {
                alert('Patient saved but not found. Please refresh the page.');
                return;
            }

        
            let assignClinicRes;
            try {
                assignClinicRes = await fetch(
                    `${API_URL}/clinics/${clinicId}/assignPatient/${createdPatient.id}`,
                    { method: 'GET' }  
                );
            } catch (err) {
                console.error('Fetch error:', err);
            }

            if (!assignClinicRes.ok) {
                const errorText = await assignClinicRes.text();
                throw new Error(`Failed to assign patient to clinic: ${errorText}`);
            }

            let assignDoctorRes;
            try {
                assignDoctorRes = await fetch(
                    `${API_URL}/doctors/${doctorId}/assignPatient/${createdPatient.id}`,
                    { method: 'GET' }  
                );
            } catch (err) {
                console.error('Fetch error details:', err);
                throw new Error(`Backend connection error: ${err.message}. Backend server (${API_URL}) is running and CORS settings are correct.`);
            }

            if (!assignDoctorRes.ok) {
                const errorText = await assignDoctorRes.text();
                throw new Error(`Failed to assign patient to doctor (Status: ${assignDoctorRes.status}): ${errorText}`);
            }

            document.getElementById('patientName').value = '';
            document.getElementById('patientSurname').value = '';
            document.getElementById('patientAge').value = '';
            document.getElementById('patientTC').value = '';
            document.getElementById('patientPhone').value = '';
            formClinicSelect.value = '';
            formDoctorSelect.innerHTML = '<option value="">Choose Doctor</option>';

            alert('Patient successfully added and assigned to clinic/doctor!');

        } catch (e) {
            console.error('Patient addition error:', e);
            alert(`Error occurred while adding patient: ${e.message || e}`);
        }
    };
}

if (window.location.pathname.includes('room.html')) {
    document.addEventListener('DOMContentLoaded', () => {
        loadClinics(loadRoomsForClinic);
    });

    async function loadRoomsForClinic(clinicId) {
        if (!clinicId) return;

        try {
            const res = await fetch(`${API_URL}/clinics/${clinicId}/rooms`);
            const rooms = await res.json();

            const table = document.getElementById('roomTable');
            if (!table) return;

            table.innerHTML = rooms.length === 0
                ? '<tr><td colspan="3">No rooms</td></tr>'
                : rooms.map(r => `
                    <tr>
                        <td>${r.roomNumber}</td>
                        <td>${r.floor}</td>
                        <td>${r.doctor ? r.doctor.name : 'Empty'}</td>
                    </tr>
                `).join('');
        } catch (e) {
            console.error('Failed to load rooms', e);
        }
    }


}


