import { Box, CircularProgress, Typography } from '@mui/material'

export function LoadingScreen() {
  return (
    <Box
      sx={{
        display: 'flex',
        flexDirection: 'column',
        alignItems: 'center',
        justifyContent: 'center',
        minHeight: '50vh',
        gap: 2,
      }}
    >
      <CircularProgress />
      <Typography color="text.secondary">Loading...</Typography>
    </Box>
  )
}
