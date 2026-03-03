<template>
  <div>
    <div class="page-header">
      <div>
        <h1>Raw Materials</h1>
        <p>Manage your stock of raw materials and inputs.</p>
      </div>
      <button class="btn btn-primary" @click="openCreateModal">+ New Raw Material</button>
    </div>

    <!-- Table -->
    <div class="card">
      <div v-if="loading" class="loading">Loading...</div>
      <div v-else-if="rawMaterials.length === 0" class="empty">No raw materials registered yet.</div>
      <table v-else class="table">
        <thead>
          <tr>
            <th>Code</th>
            <th>Name</th>
            <th>Stock Quantity</th>
            <th>Unit</th>
            <th>Actions</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="material in rawMaterials" :key="material.id">
            <td><span class="badge">{{ material.code }}</span></td>
            <td>{{ material.name }}</td>
            <td>{{ material.stockQuantity }}</td>
            <td>{{ material.unit }}</td>
            <td>
              <div class="actions">
                <button class="btn btn-sm btn-secondary" @click="openEditModal(material)">Edit</button>
                <button class="btn btn-sm btn-danger" @click="confirmDelete(material)">Delete</button>
              </div>
            </td>
          </tr>
        </tbody>
      </table>
    </div>

    <!-- Modal -->
    <div v-if="showModal" class="modal-overlay" @click.self="closeModal">
      <div class="modal">
        <div class="modal-header">
          <h2>{{ isEditing ? 'Edit Raw Material' : 'New Raw Material' }}</h2>
          <button class="modal-close" @click="closeModal">✕</button>
        </div>
        <div class="modal-body">
          <div class="form-group">
            <label>Code</label>
            <input v-model="form.code" :disabled="isEditing" placeholder="Ex: RM001" />
            <span v-if="errors.code" class="error">{{ errors.code }}</span>
          </div>
          <div class="form-group">
            <label>Name</label>
            <input v-model="form.name" placeholder="Ex: Wheat Flour" />
            <span v-if="errors.name" class="error">{{ errors.name }}</span>
          </div>
          <div class="form-row">
            <div class="form-group">
              <label>Stock Quantity</label>
              <input v-model.number="form.stockQuantity" type="number" min="0" placeholder="Ex: 500" />
              <span v-if="errors.stockQuantity" class="error">{{ errors.stockQuantity }}</span>
            </div>
            <div class="form-group">
              <label>Unit</label>
              <select v-model="form.unit">
                <option value="">Select unit</option>
                <option value="g">g</option>
                <option value="kg">kg</option>
                <option value="ml">ml</option>
                <option value="L">L</option>
                <option value="un">un</option>
              </select>
              <span v-if="errors.unit" class="error">{{ errors.unit }}</span>
            </div>
          </div>
        </div>
        <div class="modal-footer">
          <button class="btn btn-secondary" @click="closeModal">Cancel</button>
          <button class="btn btn-primary" @click="save" :disabled="saving">
            {{ saving ? 'Saving...' : 'Save' }}
          </button>
        </div>
      </div>
    </div>

    <!-- Delete Confirm Modal -->
    <div v-if="showDeleteModal" class="modal-overlay" @click.self="showDeleteModal = false">
      <div class="modal modal-sm">
        <div class="modal-header">
          <h2>Confirm Delete</h2>
          <button class="modal-close" @click="showDeleteModal = false">✕</button>
        </div>
        <div class="modal-body">
          <p>Are you sure you want to delete <strong>{{ selectedMaterial?.name }}</strong>?</p>
          <p class="warning-text">This action cannot be undone.</p>
        </div>
        <div class="modal-footer">
          <button class="btn btn-secondary" @click="showDeleteModal = false">Cancel</button>
          <button class="btn btn-danger" @click="deleteMaterial" :disabled="saving">
            {{ saving ? 'Deleting...' : 'Delete' }}
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import api from '../services/api.js'

const rawMaterials = ref([])
const loading = ref(false)
const saving = ref(false)
const showModal = ref(false)
const showDeleteModal = ref(false)
const isEditing = ref(false)
const selectedMaterial = ref(null)

const form = ref({ code: '', name: '', stockQuantity: '', unit: '' })
const errors = ref({})

async function fetchRawMaterials() {
  loading.value = true
  try {
    const response = await api.get('/raw-materials')
    rawMaterials.value = response.data
  } catch (e) {
    alert('Error loading raw materials.')
  } finally {
    loading.value = false
  }
}

function openCreateModal() {
  isEditing.value = false
  form.value = { code: '', name: '', stockQuantity: '', unit: '' }
  errors.value = {}
  showModal.value = true
}

function openEditModal(material) {
  isEditing.value = true
  selectedMaterial.value = material
  form.value = { ...material }
  errors.value = {}
  showModal.value = true
}

function closeModal() {
  showModal.value = false
  errors.value = {}
}

function validate() {
  errors.value = {}
  if (!form.value.code) errors.value.code = 'Code is required.'
  if (!form.value.name) errors.value.name = 'Name is required.'
  if (form.value.stockQuantity === '' || form.value.stockQuantity < 0) errors.value.stockQuantity = 'Enter a valid quantity.'
  if (!form.value.unit) errors.value.unit = 'Unit is required.'
  return Object.keys(errors.value).length === 0
}

async function save() {
  if (!validate()) return
  saving.value = true
  try {
    if (isEditing.value) {
      await api.put(`/raw-materials/${selectedMaterial.value.id}`, form.value)
    } else {
      await api.post('/raw-materials', form.value)
    }
    closeModal()
    await fetchRawMaterials()
  } catch (e) {
    const msg = e.response?.data?.message || 'Error saving raw material.'
    alert(msg)
  } finally {
    saving.value = false
  }
}

function confirmDelete(material) {
  selectedMaterial.value = material
  showDeleteModal.value = true
}

async function deleteMaterial() {
  saving.value = true
  try {
    await api.delete(`/raw-materials/${selectedMaterial.value.id}`)
    showDeleteModal.value = false
    await fetchRawMaterials()
  } catch (e) {
    const msg = e.response?.data?.message || 'Error deleting raw material.'
    alert(msg)
  } finally {
    saving.value = false
  }
}

onMounted(fetchRawMaterials)
</script>

<style scoped>
.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24px;
}
.page-header h1 { font-size: 24px; font-weight: 700; color: #1a1a2e; }
.page-header p { font-size: 14px; color: #777; margin-top: 4px; }
.card { background: #fff; border-radius: 12px; box-shadow: 0 2px 8px rgba(0,0,0,0.07); overflow: hidden; }
.table { width: 100%; border-collapse: collapse; }
.table th { background: #f8f9fa; padding: 12px 16px; text-align: left; font-size: 13px; font-weight: 600; color: #555; border-bottom: 1px solid #eee; }
.table td { padding: 14px 16px; font-size: 14px; border-bottom: 1px solid #f0f0f0; }
.table tr:last-child td { border-bottom: none; }
.table tr:hover td { background: #fafafa; }
.badge { background: #eef2ff; color: #4f46e5; padding: 3px 8px; border-radius: 4px; font-size: 12px; font-weight: 600; }
.actions { display: flex; gap: 8px; }
.loading, .empty { padding: 48px; text-align: center; color: #999; }
.btn { padding: 9px 18px; border-radius: 8px; border: none; cursor: pointer; font-size: 14px; font-weight: 500; transition: all 0.2s; }
.btn-primary { background: #4f46e5; color: #fff; }
.btn-primary:hover { background: #4338ca; }
.btn-secondary { background: #f1f5f9; color: #333; }
.btn-secondary:hover { background: #e2e8f0; }
.btn-danger { background: #fee2e2; color: #dc2626; }
.btn-danger:hover { background: #fecaca; }
.btn-sm { padding: 6px 12px; font-size: 13px; }
.btn:disabled { opacity: 0.6; cursor: not-allowed; }
.modal-overlay { position: fixed; inset: 0; background: rgba(0,0,0,0.5); display: flex; align-items: center; justify-content: center; z-index: 100; }
.modal { background: #fff; border-radius: 12px; width: 100%; max-width: 480px; box-shadow: 0 20px 60px rgba(0,0,0,0.2); }
.modal-sm { max-width: 380px; }
.modal-header { display: flex; justify-content: space-between; align-items: center; padding: 20px 24px; border-bottom: 1px solid #eee; }
.modal-header h2 { font-size: 18px; font-weight: 600; }
.modal-close { background: none; border: none; font-size: 18px; cursor: pointer; color: #999; }
.modal-body { padding: 24px; }
.modal-footer { display: flex; justify-content: flex-end; gap: 12px; padding: 16px 24px; border-top: 1px solid #eee; }
.form-group { display: flex; flex-direction: column; gap: 6px; margin-bottom: 16px; }
.form-group label { font-size: 13px; font-weight: 600; color: #555; }
.form-group input, .form-group select { padding: 9px 12px; border: 1px solid #ddd; border-radius: 8px; font-size: 14px; outline: none; transition: border 0.2s; }
.form-group input:focus, .form-group select:focus { border-color: #4f46e5; }
.form-row { display: grid; grid-template-columns: 1fr 1fr; gap: 16px; }
.error { font-size: 12px; color: #dc2626; }
.warning-text { font-size: 13px; color: #dc2626; margin-top: 8px; }
</style>
