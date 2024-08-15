const functions = require('firebase-functions');
const admin = require('firebase-admin');
admin.initializeApp();

exports.setCustomClaims = functions.https.onCall(async (data, context) => {
  if (!context.auth) {
    throw new functions.https.HttpsError('failed-precondition', 'The function must be called while authenticated.');
  }

  const uid = data.uid;
  const role = data.role;

  await admin.auth().setCustomUserClaims(uid, { role: role });

  return {
    message: `Success! ${role} role assigned to user with UID: ${uid}`
  };
});
