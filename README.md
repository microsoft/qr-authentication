# Project

With QR + Password, frontline workers can more easily and more quickly authenticate into a shared device by automating the process of typing a UPN (most commonly the users email address). By scanning a QR code, users can save up to 60% of the time it takes to currently type a full username. If an employee only uses their Azure Active Directory (AAD) account at work, additional steps can be taken to simplify their password to further streamline the user authentication process.

This project is meant to demonstrate how you can add this improved authentication experience to your own  Android applications. In this sample application, we show how to complete the following:
1.	Add a “Scan a QR code” button to the first screen of your app,
2.	On click of the button, open the device camera,
3.	Detect and analyze a QR code,
4.	Pass the value to the MSAL library to autopopulate the UPN field.

This solution is expected to be used in tandem with [Shared Device Mode (SDM)](https://learn.microsoft.com/en-us/azure/active-directory/develop/msal-shared-devices), a technology that automatically performs single sign-on and enables single sign-out for users on shared devices. By using SDM, employees will only need to sign-in once and get automatic access to all other applications on the device without needing to provide additional credentials.

If your employees already have badges or nametags, we recommend printing the QR codes on stickers and attaching them to the back of them. If your employees do not have badges or nametags, you can also store the QR codes in personal areas (e.g., employee locker) or in a backroom area (e.g., next to the physical time clock). Because these QR codes only contain the UPN, it is safe to put them in common employee areas.

## Prerequisites
The following pre-requisites should be completed before using this sample application of QR + Password:
1.	Authenticator must be installed on the test device – this is necessary to enable Shared Device Mode on your device.
2.	Place your device in Shared Device Mode – this is possible with both Microsoft Intune and manually using the Authenticator app on the device.
3.	Generate QR codes for the test users – please refer to the Generating QR Codes section for steps on how to generate QR codes for your employees .

## Generating QR codes
You can either manually generate your QR codes using existing QR generation websites like www.qr-code-generator.com or create a service to automatically generate QR codes for you. To create a QR code for a specific user, complete the following steps:
1.	Navigate to a QR generation website.
2.	For the value of your QR code, type in the UPN of your user (e.g., adelev@contoso.com).
3.	Download and print the generated QR code.

## About the security of the QR + Password solution
Because this solution only simplifies the entry of a user’s UPN, using this initial solution does not increase your security risk. An analogous scenario from a security perspective would be printing off and distributing a list of email addresses so that employees did not forget their UPN.

## Contributing

This project welcomes contributions and suggestions.  Most contributions require you to agree to a
Contributor License Agreement (CLA) declaring that you have the right to, and actually do, grant us
the rights to use your contribution. For details, visit https://cla.opensource.microsoft.com.

When you submit a pull request, a CLA bot will automatically determine whether you need to provide
a CLA and decorate the PR appropriately (e.g., status check, comment). Simply follow the instructions
provided by the bot. You will only need to do this once across all repos using our CLA.

This project has adopted the [Microsoft Open Source Code of Conduct](https://opensource.microsoft.com/codeofconduct/).
For more information see the [Code of Conduct FAQ](https://opensource.microsoft.com/codeofconduct/faq/) or
contact [opencode@microsoft.com](mailto:opencode@microsoft.com) with any additional questions or comments.

## Trademarks

This project may contain trademarks or logos for projects, products, or services. Authorized use of Microsoft 
trademarks or logos is subject to and must follow 
[Microsoft's Trademark & Brand Guidelines](https://www.microsoft.com/en-us/legal/intellectualproperty/trademarks/usage/general).
Use of Microsoft trademarks or logos in modified versions of this project must not cause confusion or imply Microsoft sponsorship.
Any use of third-party trademarks or logos are subject to those third-party's policies.
