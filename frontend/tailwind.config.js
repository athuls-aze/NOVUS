/** @type {import('tailwindcss').Config} */
export default {
  content: [
    "./index.html",
    "./src/**/*.{js,ts,jsx,tsx}",
  ],
  theme: {
    extend: {
      colors: {
        apple: {
          bg: '#F5F5F7',
          surface: '#FFFFFF',
          border: '#E8E8ED',
          primary: '#1D1D1F',
          secondary: '#86868B',
          blue: '#0066CC',
          green: '#34C759'
        }
      },
      boxShadow: {
        'apple-card': '0 4px 24px rgba(0, 0, 0, 0.04)',
        'apple-card-hover': '0 10px 40px rgba(0, 0, 0, 0.08)'
      },
      fontFamily: {
        sans: ['-apple-system', 'BlinkMacSystemFont', '"SF Pro Text"', 'Inter', 'sans-serif']
      }
    },
  },
  plugins: [],
}
