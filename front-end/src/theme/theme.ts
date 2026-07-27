import { createTheme } from '@mui/material/styles'

const theme = createTheme({
  palette: {
    mode: 'light',

    primary: {
      main: '#4F46E5',
      light: '#6366F1',
      dark: '#4338CA',
      contrastText: '#FFFFFF',
    },

    secondary: {
      main: '#06B6D4',
      light: '#22D3EE',
      dark: '#0891B2',
      contrastText: '#FFFFFF',
    },

    background: {
      default: '#F8FAFC',
      paper: '#FFFFFF',
    },

    text: {
      primary: '#0F172A',
      secondary: '#64748B',
    },

    success: {
      main: '#22C55E',
    },

    warning: {
      main: '#F59E0B',
    },

    error: {
      main: '#EF4444',
    },

    divider: '#E2E8F0',
  },

  shape: {
    borderRadius: 12,
  },

  typography: {
    fontFamily: ['Inter', 'Roboto', 'Helvetica', 'Arial', 'sans-serif'].join(','),
  },

  components: {
    MuiButton: {
      styleOverrides: {
        root: {
          borderRadius: 12,
          textTransform: 'none',
          fontWeight: 600,
          paddingInline: 20,
          paddingBlock: 10,
          boxShadow: 'none',

          '&:hover': {
            boxShadow: 'none',
          },
        },
      },
    },

    MuiCard: {
      styleOverrides: {
        root: {
          borderRadius: 16,
          boxShadow: '0 1px 2px rgba(15,23,42,0.06)',
          border: '1px solid #E2E8F0',
        },
      },
    },

    MuiPaper: {
      styleOverrides: {
        root: {
          backgroundImage: 'none',
        },
      },
    },

    MuiTextField: {
      defaultProps: {
        variant: 'outlined',
      },
    },

    MuiOutlinedInput: {
      styleOverrides: {
        root: {
          borderRadius: 10,

          '& fieldset': {
            borderColor: '#CBD5E1',
          },

          '&:hover fieldset': {
            borderColor: '#94A3B8',
          },

          '&.Mui-focused fieldset': {
            borderColor: '#4F46E5',
            borderWidth: 2,
          },
        },
      },
    },

    MuiAppBar: {
      styleOverrides: {
        root: {
          backgroundColor: '#FFFFFF',
          color: '#0F172A',
          boxShadow: '0 1px 2px rgba(15,23,42,0.06)',
          borderBottom: '1px solid #E2E8F0',
        },
      },
    },

    MuiTableCell: {
      styleOverrides: {
        head: {
          fontWeight: 600,
          color: '#475569',
          backgroundColor: '#F8FAFC',
        },
      },
    },
  },
})

export default theme
