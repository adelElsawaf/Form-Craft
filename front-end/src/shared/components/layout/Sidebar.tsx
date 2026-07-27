import { NavLink, useLocation } from 'react-router-dom'
import DashboardOutlinedIcon from '@mui/icons-material/DashboardOutlined'
import DynamicFormOutlinedIcon from '@mui/icons-material/DynamicFormOutlined'
import InboxOutlinedIcon from '@mui/icons-material/InboxOutlined'
import SettingsOutlinedIcon from '@mui/icons-material/SettingsOutlined'
import {
  Box,
  Drawer,
  List,
  ListItemButton,
  ListItemIcon,
  ListItemText,
  Typography,
} from '@mui/material'
import { ROUTES } from '@/shared/constants/routes'

export const DRAWER_WIDTH = 260

type NavItem = {
  label: string
  to?: string
  icon: typeof DashboardOutlinedIcon
  soon?: boolean
}

const navItems: NavItem[] = [
  { label: 'Dashboard', to: ROUTES.dashboard, icon: DashboardOutlinedIcon },
  { label: 'Forms', to: ROUTES.forms.list, icon: DynamicFormOutlinedIcon, soon: true },
  { label: 'Submissions', to: ROUTES.submissions.list, icon: InboxOutlinedIcon, soon: true },
  { label: 'Settings', to: ROUTES.settings.root, icon: SettingsOutlinedIcon, soon: true },
]

type SidebarProps = {
  mobileOpen?: boolean
  onClose?: () => void
}

function SidebarNav({ onNavigate }: { onNavigate?: () => void }) {
  const location = useLocation()

  return (
    <Box
      sx={{
        height: '100%',
        display: 'flex',
        flexDirection: 'column',
        px: 1.5,
        py: 2,
      }}
    >
      <Typography
        variant="caption"
        sx={{
          px: 1.5,
          mb: 1,
          fontWeight: 700,
          letterSpacing: '0.08em',
          textTransform: 'uppercase',
          color: 'text.secondary',
        }}
      >
        Workspace
      </Typography>

      <List disablePadding sx={{ display: 'flex', flexDirection: 'column', gap: 0.5 }}>
        {navItems.map((item) => {
          const Icon = item.icon
          const selected = Boolean(item.to && location.pathname === item.to)
          const disabled = item.soon

          return (
            <ListItemButton
              key={item.label}
              component={disabled || !item.to ? 'div' : NavLink}
              to={disabled ? undefined : item.to}
              disabled={disabled}
              onClick={disabled ? undefined : onNavigate}
              selected={selected}
              sx={{
                borderRadius: 2,
                py: 1.1,
                px: 1.5,
                color: selected ? 'primary.main' : 'text.secondary',
                bgcolor: selected ? 'rgba(79, 70, 229, 0.08)' : 'transparent',
                '&.Mui-selected': {
                  bgcolor: 'rgba(79, 70, 229, 0.08)',
                  color: 'primary.main',
                  '&:hover': {
                    bgcolor: 'rgba(79, 70, 229, 0.12)',
                  },
                },
                '&.Mui-disabled': {
                  opacity: 1,
                  color: 'text.secondary',
                },
                '&:hover': {
                  bgcolor: selected ? 'rgba(79, 70, 229, 0.12)' : 'rgba(15, 23, 42, 0.04)',
                },
              }}
            >
              <ListItemIcon
                sx={{
                  minWidth: 36,
                  color: selected ? 'primary.main' : 'text.secondary',
                }}
              >
                <Icon fontSize="small" />
              </ListItemIcon>
              <ListItemText
                primary={item.label}
                slotProps={{
                  primary: {
                    sx: {
                      fontWeight: selected ? 700 : 600,
                      fontSize: '0.9rem',
                    },
                  },
                }}
              />
              {item.soon && (
                <Box
                  sx={{
                    px: 0.9,
                    py: 0.2,
                    borderRadius: 1,
                    bgcolor: 'rgba(15, 23, 42, 0.06)',
                    border: '1px solid',
                    borderColor: 'divider',
                  }}
                >
                  <Typography
                    variant="caption"
                    sx={{ fontWeight: 700, fontSize: '0.65rem', color: 'text.secondary' }}
                  >
                    Soon
                  </Typography>
                </Box>
              )}
            </ListItemButton>
          )
        })}
      </List>

      <Box sx={{ mt: 'auto', px: 1, pt: 2 }}>
        <Box
          sx={{
            p: 2,
            borderRadius: 3,
            bgcolor: 'rgba(79, 70, 229, 0.06)',
            border: '1px dashed',
            borderColor: 'primary.main',
          }}
        >
          <Typography variant="subtitle2" sx={{ fontWeight: 800, color: 'primary.main', mb: 0.5 }}>
            Free plan
          </Typography>
          <Typography variant="caption" color="text.secondary" sx={{ lineHeight: 1.5, display: 'block' }}>
            5 forms included. Create your first form to get started.
          </Typography>
        </Box>
      </Box>
    </Box>
  )
}

export function Sidebar({ mobileOpen = false, onClose }: SidebarProps) {
  return (
    <>
      <Drawer
        variant="temporary"
        open={mobileOpen}
        onClose={onClose}
        ModalProps={{ keepMounted: true }}
        sx={{
          display: { xs: 'block', md: 'none' },
          '& .MuiDrawer-paper': {
            width: DRAWER_WIDTH,
            boxSizing: 'border-box',
            borderRight: '1px solid',
            borderColor: 'divider',
            bgcolor: 'background.paper',
          },
        }}
      >
        <SidebarNav onNavigate={onClose} />
      </Drawer>

      <Drawer
        variant="permanent"
        sx={{
          display: { xs: 'none', md: 'block' },
          width: DRAWER_WIDTH,
          flexShrink: 0,
          '& .MuiDrawer-paper': {
            position: 'relative',
            width: DRAWER_WIDTH,
            boxSizing: 'border-box',
            borderRight: '1px solid',
            borderColor: 'divider',
            bgcolor: 'background.paper',
            height: '100%',
          },
        }}
        open
      >
        <SidebarNav />
      </Drawer>
    </>
  )
}
