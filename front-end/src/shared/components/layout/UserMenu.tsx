import { useState, type MouseEvent } from 'react'
import { useNavigate } from 'react-router-dom'
import KeyboardArrowDownIcon from '@mui/icons-material/KeyboardArrowDown'
import LogoutOutlinedIcon from '@mui/icons-material/LogoutOutlined'
import DashboardOutlinedIcon from '@mui/icons-material/DashboardOutlined'
import HomeOutlinedIcon from '@mui/icons-material/HomeOutlined'
import {
  Avatar,
  Box,
  Button,
  Divider,
  ListItemIcon,
  ListItemText,
  Menu,
  MenuItem,
  Stack,
  Typography,
} from '@mui/material'
import { useAuth } from '@/features/auth/hooks/useAuth'
import { ROUTES } from '@/shared/constants/routes'
import { formatFullName } from '@/shared/lib/formatters'

function getInitials(firstName: string, lastName: string) {
  return `${firstName.charAt(0)}${lastName.charAt(0)}`.toUpperCase()
}

type UserMenuProps = {
  /** Extra menu item for the current context */
  variant?: 'public' | 'app'
  fullWidth?: boolean
}

export function UserMenu({ variant = 'public', fullWidth = false }: UserMenuProps) {
  const { user, logout } = useAuth()
  const navigate = useNavigate()
  const [anchorEl, setAnchorEl] = useState<null | HTMLElement>(null)
  const menuOpen = Boolean(anchorEl)

  if (!user) {
    return null
  }

  const fullName = formatFullName(user.firstName, user.lastName)

  const handleOpenMenu = (event: MouseEvent<HTMLElement>) => {
    setAnchorEl(event.currentTarget)
  }

  const handleCloseMenu = () => {
    setAnchorEl(null)
  }

  const handleLogout = async () => {
    handleCloseMenu()
    await logout()
    void navigate(ROUTES.home, { replace: true })
  }

  return (
    <Box
      sx={{
        width: fullWidth ? '100%' : 'auto',
        display: fullWidth
          ? 'block'
          : { xs: 'none', md: 'block' },
      }}
    >
      <Button
        onClick={handleOpenMenu}
        fullWidth={fullWidth}
        endIcon={<KeyboardArrowDownIcon sx={{ fontSize: 18, color: 'text.secondary' }} />}
        sx={{
          color: 'text.primary',
          px: 1.25,
          py: 0.75,
          borderRadius: 2,
          border: '1px solid',
          borderColor: 'divider',
          bgcolor: 'background.default',
          justifyContent: fullWidth ? 'space-between' : 'center',
          '&:hover': {
            bgcolor: 'rgba(79, 70, 229, 0.04)',
            borderColor: 'primary.main',
          },
        }}
      >
        <Stack direction="row" spacing={1.25} sx={{ alignItems: 'center' }}>
          <Avatar
            sx={{
              width: 32,
              height: 32,
              fontSize: '0.8rem',
              fontWeight: 700,
              bgcolor: 'primary.main',
            }}
          >
            {getInitials(user.firstName, user.lastName)}
          </Avatar>
          <Box sx={{ textAlign: 'left', display: fullWidth ? 'block' : { xs: 'none', sm: 'block' } }}>
            <Typography variant="body2" sx={{ fontWeight: 700, lineHeight: 1.2 }}>
              {fullName}
            </Typography>
            <Typography variant="caption" color="text.secondary" sx={{ lineHeight: 1.2 }}>
              {user.email}
            </Typography>
          </Box>
        </Stack>
      </Button>

      <Menu
        anchorEl={anchorEl}
        open={menuOpen}
        onClose={handleCloseMenu}
        anchorOrigin={{ vertical: 'bottom', horizontal: fullWidth ? 'left' : 'right' }}
        transformOrigin={{ vertical: 'top', horizontal: fullWidth ? 'left' : 'right' }}
        slotProps={{
          paper: {
            sx: {
              mt: 1,
              minWidth: fullWidth ? '100%' : 220,
              width: fullWidth ? 'calc(100% - 2rem)' : undefined,
              borderRadius: 2.5,
              border: '1px solid',
              borderColor: 'divider',
              boxShadow: '0 12px 32px rgba(15, 23, 42, 0.1)',
            },
          },
        }}
      >
        <Box sx={{ px: 2, py: 1.5 }}>
          <Typography variant="subtitle2" sx={{ fontWeight: 700 }}>
            {fullName}
          </Typography>
          <Typography variant="caption" color="text.secondary">
            {user.email}
          </Typography>
        </Box>
        <Divider />
        {variant === 'public' ? (
          <MenuItem
            onClick={() => {
              handleCloseMenu()
              void navigate(ROUTES.dashboard)
            }}
            sx={{ py: 1.25 }}
          >
            <ListItemIcon sx={{ minWidth: 36 }}>
              <DashboardOutlinedIcon fontSize="small" />
            </ListItemIcon>
            <ListItemText primary="Go to dashboard" />
          </MenuItem>
        ) : (
          <MenuItem
            onClick={() => {
              handleCloseMenu()
              void navigate(ROUTES.home)
            }}
            sx={{ py: 1.25 }}
          >
            <ListItemIcon sx={{ minWidth: 36 }}>
              <HomeOutlinedIcon fontSize="small" />
            </ListItemIcon>
            <ListItemText primary="View homepage" />
          </MenuItem>
        )}
        <MenuItem onClick={() => void handleLogout()} sx={{ py: 1.25, color: 'error.main' }}>
          <ListItemIcon sx={{ color: 'inherit', minWidth: 36 }}>
            <LogoutOutlinedIcon fontSize="small" />
          </ListItemIcon>
          <ListItemText primary="Log out" />
        </MenuItem>
      </Menu>
    </Box>
  )
}
