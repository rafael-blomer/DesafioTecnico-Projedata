<template>
  <div>
    <div class="page-header">
      <div>
        <h1>Production Optimizer</h1>
        <p>Analyze your current stock and find the best production plan to maximize revenue.</p>
      </div>
      <button class="btn btn-primary" @click="optimize" :disabled="loading">
        {{ loading ? 'Analyzing...' : '🚀 Run Optimizer' }}
      </button>
    </div>

    <!-- Empty state -->
    <div v-if="!result && !loading" class="empty-state">
      <div class="empty-icon">🏭</div>
      <h2>Ready to optimize</h2>
      <p>Click "Run Optimizer" to analyze your current stock and get a production suggestion.</p>
    </div>

    <!-- Loading -->
    <div v-if="loading" class="empty-state">
      <div class="empty-icon">⏳</div>
      <h2>Analyzing stock...</h2>
      <p>Calculating the best production plan for maximum revenue.</p>
    </div>

    <!-- Result -->
    <div v-if="result && !loading">

      <!-- Total Value Card -->
      <div class="total-card">
        <div class="total-info">
          <span class="total-label">Estimated Total Sale Value</span>
          <span class="total-value">{{ formatPrice(result.totalValue) }}</span>
        </div>
        <div class="total-icon">💰</div>
      </div>

      <!-- No products suggestion -->
      <div v-if="result.items.length === 0" class="empty-state">
        <div class="empty-icon">⚠️</div>
        <h2>No products can be produced</h2>
        <p>Your current stock is insufficient to produce any registered product. Please restock your raw materials.</p>
      </div>

      <!-- Items Table -->
      <div v-else class="card">
        <div class="card-header">
          <h2>Production Plan</h2>
          <span class="items-count">{{ result.items.length }} product(s)</span>
        </div>
        <table class="table">
          <thead>
            <tr>
              <th>#</th>
              <th>Code</th>
              <th>Product</th>
              <th>Unit Price</th>
              <th>Qty to Produce</th>
              <th>Total Value</th>
              <th>Share</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="(item, index) in result.items" :key="item.productCode">
              <td class="rank">{{ index + 1 }}</td>
              <td><span class="badge">{{ item.productCode }}</span></td>
              <td class="product-name">{{ item.productName }}</td>
              <td>{{ formatPrice(item.unitPrice) }}</td>
              <td><span class="qty-badge">× {{ item.quantityToProduce }}</span></td>
              <td class="item-total">{{ formatPrice(item.totalItemValue) }}</td>
              <td>
                <div class="progress-bar">
                  <div
                    class="progress-fill"
                    :style="{ width: getShare(item.totalItemValue) + '%' }"
                  ></div>
                  <span class="progress-label">{{ getShare(item.totalItemValue) }}%</span>
                </div>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import api from '../services/api.js'

const result = ref(null)
const loading = ref(false)

async function optimize() {
  loading.value = true
  result.value = null
  try {
    const response = await api.get('/production/optimize')
    result.value = response.data
  } catch (e) {
    alert('Error running optimizer.')
  } finally {
    loading.value = false
  }
}

function formatPrice(value) {
  return new Intl.NumberFormat('en-US', { style: 'currency', currency: 'USD' }).format(value)
}

function getShare(itemValue) {
  if (!result.value || result.value.totalValue === 0) return 0
  return Math.round((itemValue / result.value.totalValue) * 100)
}
</script>

<style scoped>
.page-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 24px; }
.page-header h1 { font-size: 24px; font-weight: 700; color: #1a1a2e; }
.page-header p { font-size: 14px; color: #777; margin-top: 4px; }

.btn { padding: 10px 20px; border-radius: 8px; border: none; cursor: pointer; font-size: 14px; font-weight: 500; transition: all 0.2s; }
.btn-primary { background: #4f46e5; color: #fff; }
.btn-primary:hover { background: #4338ca; }
.btn:disabled { opacity: 0.6; cursor: not-allowed; }

.empty-state { text-align: center; padding: 64px 24px; }
.empty-icon { font-size: 56px; margin-bottom: 16px; }
.empty-state h2 { font-size: 20px; font-weight: 600; color: #1a1a2e; margin-bottom: 8px; }
.empty-state p { font-size: 14px; color: #777; max-width: 400px; margin: 0 auto; }

.total-card {
  background: linear-gradient(135deg, #4f46e5, #7c3aed);
  border-radius: 12px;
  padding: 28px 32px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24px;
  box-shadow: 0 8px 24px rgba(79,70,229,0.3);
}
.total-info { display: flex; flex-direction: column; gap: 6px; }
.total-label { font-size: 14px; color: rgba(255,255,255,0.8); font-weight: 500; }
.total-value { font-size: 36px; font-weight: 700; color: #fff; }
.total-icon { font-size: 48px; }

.card { background: #fff; border-radius: 12px; box-shadow: 0 2px 8px rgba(0,0,0,0.07); overflow: hidden; }
.card-header { display: flex; justify-content: space-between; align-items: center; padding: 16px 20px; border-bottom: 1px solid #eee; }
.card-header h2 { font-size: 16px; font-weight: 600; color: #1a1a2e; }
.items-count { font-size: 13px; color: #777; background: #f1f5f9; padding: 4px 10px; border-radius: 20px; }

.table { width: 100%; border-collapse: collapse; }
.table th { background: #f8f9fa; padding: 12px 16px; text-align: left; font-size: 13px; font-weight: 600; color: #555; border-bottom: 1px solid #eee; }
.table td { padding: 14px 16px; font-size: 14px; border-bottom: 1px solid #f0f0f0; }
.table tr:last-child td { border-bottom: none; }
.table tr:hover td { background: #fafafa; }

.rank { font-weight: 700; color: #4f46e5; }
.badge { background: #eef2ff; color: #4f46e5; padding: 3px 8px; border-radius: 4px; font-size: 12px; font-weight: 600; }
.product-name { font-weight: 500; }
.qty-badge { background: #f0fdf4; color: #16a34a; border: 1px solid #bbf7d0; padding: 4px 10px; border-radius: 6px; font-size: 13px; font-weight: 600; }
.item-total { font-weight: 600; color: #16a34a; }

.progress-bar { display: flex; align-items: center; gap: 8px; min-width: 140px; }
.progress-fill { height: 6px; background: linear-gradient(90deg, #4f46e5, #7c3aed); border-radius: 3px; transition: width 0.5s; flex-shrink: 0; }
.progress-label { font-size: 12px; color: #777; white-space: nowrap; }
</style>
