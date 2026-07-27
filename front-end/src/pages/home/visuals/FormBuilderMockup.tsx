import { useState } from 'react'
import { Box, Stack, Typography } from '@mui/material'
import DynamicFormIcon from '@mui/icons-material/DynamicForm'

type FieldType = 'text' | 'email' | 'dropdown' | 'checkbox'

type FormField = {
  paletteLabel: string
  label: string
  type: FieldType
}

const formFields: FormField[] = [
  { paletteLabel: 'Short text', label: 'Full name', type: 'text' },
  { paletteLabel: 'Email', label: 'Work email', type: 'email' },
  { paletteLabel: 'Dropdown', label: 'Ticket type', type: 'dropdown' },
  { paletteLabel: 'Checkbox', label: 'Send me event updates', type: 'checkbox' },
]

function FieldPreview({ field, isActive }: { field: FormField; isActive: boolean }) {
  const activeSx = {
    borderColor: isActive ? 'primary.main' : 'divider',
    bgcolor: isActive ? 'rgba(79, 70, 229, 0.06)' : 'background.default',
  }

  if (field.type === 'checkbox') {
    return (
      <Box
        sx={{
          mt: 0.5,
          display: 'flex',
          alignItems: 'center',
          gap: 1,
          p: 1,
          borderRadius: 2,
          border: '1px solid',
          transition: 'all 0.2s ease',
          ...activeSx,
        }}
      >
        <Box
          sx={{
            width: 16,
            height: 16,
            borderRadius: 0.75,
            border: '2px solid',
            borderColor: isActive ? 'primary.main' : 'divider',
            bgcolor: isActive ? 'primary.main' : 'transparent',
            flexShrink: 0,
          }}
        />
        <Typography variant="caption" color="text.secondary">
          {field.label}
        </Typography>
      </Box>
    )
  }

  if (field.type === 'dropdown') {
    return (
      <Box
        sx={{
          mt: 0.5,
          height: 36,
          borderRadius: 2,
          border: '1px solid',
          display: 'flex',
          alignItems: 'center',
          px: 1.25,
          transition: 'all 0.2s ease',
          ...activeSx,
        }}
      >
        <Typography variant="caption" color="text.secondary">
          General admission
        </Typography>
        <Box sx={{ ml: 'auto', width: 0, height: 0, borderLeft: '4px solid transparent', borderRight: '4px solid transparent', borderTop: '5px solid', borderTopColor: 'text.secondary' }} />
      </Box>
    )
  }

  return (
    <Box
      sx={{
        mt: 0.5,
        height: 32,
        borderRadius: 2,
        border: '1px solid',
        transition: 'all 0.2s ease',
        ...activeSx,
      }}
    />
  )
}

export function FormBuilderMockup() {
  const [activeField, setActiveField] = useState(0)

  return (
    <Box
      sx={{
        position: 'relative',
        width: '100%',
        maxWidth: 520,
        mx: 'auto',
        animation: 'float 6s ease-in-out infinite',
        '@keyframes float': {
          '0%, 100%': { transform: 'translateY(0)' },
          '50%': { transform: 'translateY(-10px)' },
        },
      }}
    >
      <Box
        sx={{
          borderRadius: 4,
          overflow: 'hidden',
          border: '1px solid',
          borderColor: 'divider',
          bgcolor: 'background.paper',
          boxShadow: '0 24px 60px rgba(15, 23, 42, 0.12)',
        }}
      >
        <Box
          sx={{
            px: 2,
            py: 1.5,
            display: 'flex',
            alignItems: 'center',
            gap: 1,
            borderBottom: '1px solid',
            borderColor: 'divider',
            bgcolor: 'background.default',
          }}
        >
          <Box sx={{ display: 'flex', gap: 0.75 }}>
            {['#EF4444', '#F59E0B', '#22C55E'].map((color) => (
              <Box key={color} sx={{ width: 10, height: 10, borderRadius: '50%', bgcolor: color }} />
            ))}
          </Box>
          <Typography variant="caption" color="text.secondary" sx={{ ml: 1 }}>
            formcraft.app/builder
          </Typography>
        </Box>

        <Box sx={{ display: 'flex', minHeight: 300 }}>
          <Box
            sx={{
              width: 130,
              p: 1.5,
              borderRight: '1px solid',
              borderColor: 'divider',
              bgcolor: 'rgba(79, 70, 229, 0.03)',
            }}
          >
            <Typography variant="caption" sx={{ fontWeight: 700, color: 'text.secondary', mb: 1, display: 'block' }}>
              Fields
            </Typography>
            <Stack spacing={1}>
              {formFields.map((field, index) => (
                <Box
                  key={field.paletteLabel}
                  onMouseEnter={() => setActiveField(index)}
                  sx={{
                    px: 1.25,
                    py: 0.75,
                    borderRadius: 2,
                    bgcolor: activeField === index ? 'rgba(79, 70, 229, 0.1)' : 'background.paper',
                    border: '1px solid',
                    borderColor: activeField === index ? 'primary.main' : 'divider',
                    fontSize: '0.72rem',
                    fontWeight: 600,
                    color: activeField === index ? 'primary.main' : 'text.secondary',
                    cursor: 'pointer',
                    transition: 'all 0.2s ease',
                    '&:hover': {
                      borderColor: 'primary.main',
                      color: 'primary.main',
                    },
                  }}
                >
                  {field.paletteLabel}
                </Box>
              ))}
            </Stack>
          </Box>

          <Box sx={{ flex: 1, p: 2.5 }}>
            <Stack direction="row" spacing={1} sx={{ alignItems: 'center', mb: 2 }}>
              <Box
                sx={{
                  width: 32,
                  height: 32,
                  borderRadius: 1.5,
                  bgcolor: 'primary.main',
                  color: 'primary.contrastText',
                  display: 'grid',
                  placeItems: 'center',
                }}
              >
                <DynamicFormIcon sx={{ fontSize: 18 }} />
              </Box>
              <Typography variant="subtitle2" sx={{ fontWeight: 700 }}>
                Event registration
              </Typography>
            </Stack>

            <Stack spacing={1.5}>
              {formFields.map((field, index) => (
                <Box key={field.label}>
                  <Typography variant="caption" sx={{ fontWeight: 600, color: 'text.secondary' }}>
                    {field.type === 'checkbox' ? '' : field.label}
                  </Typography>
                  <FieldPreview field={field} isActive={activeField === index} />
                </Box>
              ))}

              <Box
                sx={{
                  mt: 0.5,
                  width: 96,
                  py: 0.75,
                  borderRadius: 2,
                  bgcolor: 'primary.main',
                  transition: 'transform 0.2s ease',
                  '&:hover': { transform: 'scale(1.03)' },
                }}
              />
            </Stack>
          </Box>
        </Box>
      </Box>

      <Box
        aria-hidden
        sx={{
          position: 'absolute',
          width: 160,
          height: 160,
          borderRadius: '50%',
          bgcolor: 'rgba(6, 182, 212, 0.15)',
          filter: 'blur(40px)',
          bottom: -40,
          left: -40,
          zIndex: -1,
        }}
      />
    </Box>
  )
}
