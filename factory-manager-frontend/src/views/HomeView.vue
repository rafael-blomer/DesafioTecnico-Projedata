<template>
  <div class="home">
    <div class="hero">
      <h1>Welcome to Factory Optimizer</h1>
      <p>Manage your raw materials and products, and discover the best production plan to maximize your revenue.</p>
    </div>
    <div class="cards">
      <RouterLink to="/raw-materials" class="card">
        <div class="card-icon">🧪</div>
        <h2>Raw Materials</h2>
        <p>Manage your stock of raw materials and inputs.</p>
      </RouterLink>
      <RouterLink to="/products" class="card">
        <div class="card-icon">📦</div>
        <h2>Products</h2>
        <p>Register products and define their compositions.</p>
      </RouterLink>
      <RouterLink to="/optimizer" class="card">
        <div class="card-icon">🚀</div>
        <h2>Optimizer</h2>
        <p>Find out what to produce to maximize your total sale value.</p>
      </RouterLink>
    </div>
  </div>
</template>

<style scoped>
.hero {
  text-align: center;
  padding: 48px 0 40px;
}

.hero h1 {
  font-size: 32px;
  font-weight: 700;
  color: #1a1a2e;
  margin-bottom: 12px;
}

.hero p {
  font-size: 16px;
  color: #666;
  max-width: 520px;
  margin: 0 auto;
}

.cards {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 24px;
  margin-top: 16px;
}

.card {
  background: #fff;
  border-radius: 12px;
  padding: 32px 24px;
  text-decoration: none;
  color: inherit;
  box-shadow: 0 2px 8px rgba(0,0,0,0.07);
  transition: all 0.2s;
  text-align: center;
  border: 2px solid transparent;
}

.card:hover {
  transform: translateY(-4px);
  border-color: #4f46e5;
  box-shadow: 0 8px 24px rgba(79,70,229,0.15);
}

.card-icon {
  font-size: 40px;
  margin-bottom: 16px;
}

.card h2 {
  font-size: 18px;
  font-weight: 600;
  margin-bottom: 8px;
  color: #1a1a2e;
}

.card p {
  font-size: 14px;
  color: #777;
  line-height: 1.5;
}
</style>
