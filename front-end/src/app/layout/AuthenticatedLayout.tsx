import { useState } from 'react'
import MenuIcon from '@mui/icons-material/Menu'
import { Box, Container } from '@mui/material'
import { Outlet } from 'react-router-dom'
import { AppNavbar } from '@/shared/components/layout/AppNavbar'
import { Sidebar } from '@/shared/components/layout/Sidebar'

export function AuthenticatedLayout() {
  const [mobileOpen, setMobileOpen] = useState(false)

  return (
    <Box
      sx={{
        minHeight: '100vh',
        display: 'flex',
        flexDirection: 'column',
        bgcolor: 'background.default',
      }}
    >
      <AppNavbar
        menuIcon={<MenuIcon />}
        onMenuClick={() => setMobileOpen(true)}
      />

      <Box sx={{ display: 'flex', flex: 1, minHeight: 0 }}>
        <Sidebar mobileOpen={mobileOpen} onClose={() => setMobileOpen(false)} />

        <Box
          component="main"
          sx={{
            flex: 1,
            minWidth: 0,
            overflow: 'auto',
            background:
              'radial-gradient(circle at 0% 0%, rgba(79, 70, 229, 0.05), transparent 42%), radial-gradient(circle at 100% 0%, rgba(6, 182, 212, 0.05), transparent 36%)',
          }}
        >
          <Container maxWidth="lg" sx={{ py: { xs: 3, md: 4 }, px: { xs: 2, md: 3 } }}>
            <Outlet />
          </Container>
        </Box>
      </Box>
    </Box>
  )
}
