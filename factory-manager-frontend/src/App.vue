<template>
  <div id="app">
    <nav class="navbar">
      <div class="navbar-brand">
        <span class="brand-icon">🏭</span>
        <span class="brand-name">Factory Optimizer</span>
      </div>
      <div class="navbar-links">
        <RouterLink to="/">Home</RouterLink>
        <RouterLink to="/raw-materials">Raw Materials</RouterLink>
        <RouterLink to="/products">Products</RouterLink>
        <RouterLink to="/optimizer">Optimizer</RouterLink>
      </div>
    </nav>
    <main class="main-content">
      <RouterView />
    </main>
  </div>
</template>

<style>
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

body {
  font-family: 'Segoe UI', sans-serif;
  background-color: #f4f6f9;
  color: #333;
}

.navbar {
  display: flex;
  align-items: center;
  justify-content: space-between;
  background-color: #1a1a2e;
  padding: 0 32px;
  height: 64px;
  box-shadow: 0 2px 8px rgba(0,0,0,0.3);
}

.navbar-brand {
  display: flex;
  align-items: center;
  gap: 10px;
}

.brand-icon {
  font-size: 24px;
}

.brand-name {
  color: #e2e8f0;
  font-size: 20px;
  font-weight: 700;
  letter-spacing: 0.5px;
}

.navbar-links {
  display: flex;
  gap: 8px;
}

.navbar-links a {
  color: #a0aec0;
  text-decoration: none;
  padding: 8px 16px;
  border-radius: 6px;
  font-size: 14px;
  font-weight: 500;
  transition: all 0.2s;
}

.navbar-links a:hover {
  background-color: rgba(255,255,255,0.1);
  color: #fff;
}

.navbar-links a.router-link-active {
  background-color: #4f46e5;
  color: #fff;
}

.main-content {
  max-width: 1200px;
  margin: 0 auto;
  padding: 32px 24px;
}
</style>
