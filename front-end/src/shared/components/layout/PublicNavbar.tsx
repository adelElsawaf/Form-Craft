import { useState, type MouseEvent } from 'react'
import { Link as RouterLink, useLocation, useNavigate } from 'react-router-dom'
import DynamicFormIcon from '@mui/icons-material/DynamicForm'
import MenuIcon from '@mui/icons-material/Menu'
import CloseIcon from '@mui/icons-material/Close'
import {
  AppBar,
  Box,
  Button,
  Container,
  Drawer,
  IconButton,
  List,
  ListItemButton,
  ListItemText,
  Stack,
  Toolbar,
  Typography,
} from '@mui/material'
import { useAuth } from '@/features/auth/hooks/useAuth'
import { ScrollNavLink } from '@/shared/components/layout/ScrollNavLink'
import { UserMenu } from '@/shared/components/layout/UserMenu'
import { PUBLIC_NAV_LINKS, ROUTES } from '@/shared/constants/routes'
import { homeSectionHash, scrollToSection } from '@/shared/lib/scrollToSection'

function Logo() {
  return (
    <Box
      component={RouterLink}
      to={ROUTES.home}
      sx={{
        display: 'inline-flex',
        alignItems: 'center',
        gap: 1,
        textDecoration: 'none',
        color: 'inherit',
      }}
    >
      <Box
        sx={{
          width: 36,
          height: 36,
          borderRadius: 2,
          display: 'grid',
          placeItems: 'center',
          bgcolor: 'primary.main',
          color: 'primary.contrastText',
        }}
      >
        <DynamicFormIcon sx={{ fontSize: 22 }} />
      </Box>
      <Typography
        variant="h6"
        sx={{
          fontWeight: 700,
          letterSpacing: '-0.02em',
          color: 'text.primary',
          display: { xs: 'none', sm: 'block' },
        }}
      >
        FormCraft
      </Typography>
    </Box>
  )
}

function DesktopNavLinks() {
  return (
    <Stack
      component="nav"
      direction="row"
      spacing={0.5}
      aria-label="Main navigation"
      sx={{ display: { xs: 'none', md: 'flex' } }}
    >
      {PUBLIC_NAV_LINKS.map((link) => (
        <ScrollNavLink key={link.sectionId} sectionId={link.sectionId} label={link.label} />
      ))}
    </Stack>
  )
}

function GuestAuthButtons({ fullWidth = false }: { fullWidth?: boolean }) {
  return (
    <Stack
      direction={fullWidth ? 'column' : 'row'}
      spacing={1.5}
      sx={{
        width: fullWidth ? '100%' : 'auto',
        display: { xs: fullWidth ? 'flex' : 'none', md: fullWidth ? 'none' : 'flex' },
      }}
    >
      <Button
        component={RouterLink}
        to={ROUTES.auth.login}
        variant="outlined"
        color="primary"
        fullWidth={fullWidth}
        sx={{
          borderColor: 'divider',
          color: 'text.primary',
          '&:hover': {
            borderColor: 'primary.main',
            bgcolor: 'rgba(79, 70, 229, 0.04)',
          },
        }}
      >
        Login
      </Button>
      <Button
        component={RouterLink}
        to={ROUTES.auth.register}
        variant="contained"
        color="primary"
        fullWidth={fullWidth}
      >
        Register
      </Button>
    </Stack>
  )
}

function NavAuthActions({ fullWidth = false }: { fullWidth?: boolean }) {
  const { user, isLoading } = useAuth()

  if (user) {
    return <UserMenu variant="public" fullWidth={fullWidth} />
  }

  if (isLoading) {
    return null
  }

  return <GuestAuthButtons fullWidth={fullWidth} />
}

export function PublicNavbar() {
  const [mobileOpen, setMobileOpen] = useState(false)
  const location = useLocation()
  const navigate = useNavigate()

  const closeMobile = () => setMobileOpen(false)

  const handleMobileSectionClick = (sectionId: (typeof PUBLIC_NAV_LINKS)[number]['sectionId']) => {
    closeMobile()
    const hash = homeSectionHash(sectionId)

    if (location.pathname === ROUTES.home) {
      scrollToSection(sectionId)
      navigate({ pathname: ROUTES.home, hash }, { replace: true })
      return
    }

    navigate({ pathname: ROUTES.home, hash })
  }

  return (
    <>
      <AppBar position="sticky" elevation={0}>
        <Container maxWidth="lg">
          <Toolbar disableGutters sx={{ minHeight: { xs: 64, md: 72 }, gap: 2 }}>
            <Logo />

            <Box
              sx={{
                flex: 1,
                display: { xs: 'none', md: 'flex' },
                justifyContent: 'center',
              }}
            >
              <DesktopNavLinks />
            </Box>

            <NavAuthActions />

            <IconButton
              aria-label="Open navigation menu"
              onClick={() => setMobileOpen(true)}
              sx={{
                display: { md: 'none' },
                ml: 'auto',
                border: '1px solid',
                borderColor: 'divider',
                borderRadius: 2,
              }}
            >
              <MenuIcon />
            </IconButton>
          </Toolbar>
        </Container>
      </AppBar>

      <Drawer
        anchor="right"
        open={mobileOpen}
        onClose={closeMobile}
        slotProps={{
          paper: {
            sx: {
              width: 'min(100vw - 2rem, 320px)',
              p: 2,
            },
          },
        }}
      >
        <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', mb: 2 }}>
          <Logo />
          <IconButton aria-label="Close navigation menu" onClick={closeMobile}>
            <CloseIcon />
          </IconButton>
        </Box>

        <List component="nav" aria-label="Mobile navigation">
          {PUBLIC_NAV_LINKS.map((link) => (
            <ListItemButton
              key={link.sectionId}
              component="a"
              href={`${ROUTES.home}${homeSectionHash(link.sectionId)}`}
              onClick={(event: MouseEvent<HTMLAnchorElement>) => {
                event.preventDefault()
                handleMobileSectionClick(link.sectionId)
              }}
              sx={{
                borderRadius: 2,
                mb: 0.5,
              }}
            >
              <ListItemText
                primary={link.label}
                slotProps={{
                  primary: {
                    sx: { fontWeight: 500 },
                  },
                }}
              />
            </ListItemButton>
          ))}
        </List>

        <Box sx={{ mt: 'auto', pt: 3 }}>
          <NavAuthActions fullWidth />
        </Box>
      </Drawer>
    </>
  )
}
