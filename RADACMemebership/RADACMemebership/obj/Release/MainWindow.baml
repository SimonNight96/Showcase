   M S B A M L   `   `   `  ���� K  GRADACMemebership, Version=1.0.0.0, Culture=neutral, PublicKeyToken=null3clr-namespace:RADACMemebershipRADACMemebership  !    RADACMemebership.MainWindow   \ XPresentationFramework, Version=4.0.0.0, Culture=neutral, PublicKeyToken=31bf3856ad364e35R NWindowsBase, Version=4.0.0.0, Culture=neutral, PublicKeyToken=31bf3856ad364e35W SPresentationCore, Version=4.0.0.0, Culture=neutral, PublicKeyToken=31bf3856ad364e35D 9http://schemas.microsoft.com/winfx/2006/xaml/presentation    R NSystem.Xaml, Version=4.0.0.0, Culture=neutral, PublicKeyToken=b77a5c561934e089:x,http://schemas.microsoft.com/winfx/2006/xaml     @d2http://schemas.microsoft.com/expression/blend/2008     Jmc;http://schemas.openxmlformats.org/markup-compatibility/2006     0localclr-namespace:RADACMemebership       � Title$  
MainWindow��$	��448��$��1288.087��.���  � ColumnDefinitions �� $	��30* ��� $
��227* ��� $��1023* �.+��� -    ��Name$ 
tabControl��$
��Left=�$	��418��$	��Top=�$
��1280��$��3��.@��� $��View Database��.��� ���	#FFE5E5E5$��0,0,-619,-13q� �� $
��123* ��� $
��199* ��� $��1567* �.+�&   System.Windows.Controls.DataGrid  -     Name$ MemberDataGrid��$
��Left=�$��5,13,0,0q�$	��Top=�$	��256��$
��1255��$��3��   
IsReadOnly . �� -    ��Name$ label��$
��Key:��$
��Left=�$��
70,287,0,0q�$	��Top=�$
��Bold"��� 
������$
��Left=�$��22��$��107,291,0,0q�
���   �$	��Top=�$��50��$��2���� 
����·�$
��Left=�$��22��$��107,318,0,0q�
���   �$	��Top=�$��50��$��2���� 
����p��$
��Left=�$��22��$��107,344,0,0q�
���   �$	��Top=�$��50��$��2���� -   $ label1��$2��,- Member should be paying senior membership.��$
��Left=�$	��Top=�$��1��$��
34,290,0,0q�$��2���� -   $ label1_Copy��$2��,- Member should be paying junior membership.��$
��Left=�$	��Top=�$��1��$��
34,316,0,0q�$��2���� -   $ label1_Copy1��$2��,- Member should be paying senior membership.��$
��Left=�$	��Top=�$��1��$��
34,340,0,0q�$��2���� -    ��Name$ EmailBtn��$��	Email all��$��2��$
��Left=�$��170,313,0,0q�$	��Top=�$	��112��$��29���� $��
Add Member��.��� ���	#FFE5E5E5 �� $	��96* ��� $
��440* ��� $	��99* �.+��� -    ��Name$ FirstNameTbx��$
��Left=�$��23��$��	87,38,0,0q� �� TextWrapping$
 Wrap=�$	��Top=�$	��120��$��2���� -	   $ SecondNameTbx��$
��Left=�$��23��$��	87,85,0,0q�$
 Wrap=�$	��Top=�$	��120��$��2���� -
   $ 	InitalTbx��$
��Left=�$��23��$��
87,132,0,0q�$
 Wrap=�$	��Top=�$	��120��$��2���� -   	 ��Name$	 
GenderCmbx��$
��Left=�$��	97,38,0,0q�$	��Top=�$	��120��
 �� 
IsReadOnly
 . $��1���� -   $ DataOfBirthTbx��$
��Left=�$��23��$��	97,85,0,0q�$
 Wrap=�$	��Top=�$	��120��$��1���� -   $	 MemberShipClassCmbx��$
��Left=�$��
97,133,0,0q�$	��Top=�$	��120��
 . $��1���� -   $	 TypeOfFishingCmbx��$
��Left=�$��
97,181,0,0q�$	��Top=�$	��120��
 . $��1���� -   $ DateOfJoiningTbx��$
��Left=�$��23��$��
259,38,0,0q�$
 Wrap=�$	��Top=�$	��120��$��1���� -   $	 MemberShipStatusCbmx��$
��Left=�$��
259,86,0,0q�$	��Top=�$	��120��
 . $��1���� -   $ FirstLineOfAddressTbx��$
��Left=�$��23��$��259,133,0,0q�$
 Wrap=�$	��Top=�$	��120��$��1���� -   $ SecondLineofAddressTbx��$
��Left=�$��23��$��259,181,0,0q�$
 Wrap=�$	��Top=�$	��120��$��1���� -   $ ThirdLineAddressTbx��$
��Left=�$��23��$��
409,38,0,0q�$
 Wrap=�$	��Top=�$	��120��$��1���� -   $ PostCodeTbx��$
��Left=�$��23��$��
409,86,0,0q�$
 Wrap=�$	��Top=�$	��120��$��1���� -   $ EmailAddressTbx��$
��Left=�$��23��$��409,133,0,0q�$
 Wrap=�$	��Top=�$	��120��$��1���� -   $ 	CarRegTbx��$
��Left=�$��23��$��409,181,0,0q�$
 Wrap=�$	��Top=�$	��120��$��1���� -   $ 
AddUserBtn��$	��Add��$
��Left=�$��
594,59,0,0q�$	��Top=�$��75��$��1���� -   $ TelephoneTbx��$��1��$
��Left=�$��23��$��409,229,0,0q�$
 Wrap=�$	��Top=�$	��120���� -   $ NotesTbx��$��1��$
��Left=�$��75��$��409,269,0,0q�$
 Wrap=�$	��Top=�$	��281���� $��Edit Record��$
��Left=�$��20��$	��Top=�$��88��$��-2,0,0,0q�.��� ���	#FFE5E5E5 �� $	��17* ��� $
��117* ��� $��1136* �.+��� -   $ 	SelectBtn��$��Select��$
��Left=�$��	52,23,0,0q�$	��Top=�$��75��$��2���� -   $ IDTbx��$
��Left=�$��23��$��	22,23,0,0q�$
 Wrap=�$	��Top=�$	��120��$��2��$��1���� -   $ EditFirstNameTbx��$
��Left=�$��23��$��	23,72,0,0q�$
 Wrap=�$	��Top=�$	��120��$��2��$��1���� -   $ EditSecondNameTbx��$
��Left=�$��23��$��
23,119,0,0q�$
 Wrap=�$	��Top=�$	��120��$��2��$��1���� -   $ EditInitalTbx��$
��Left=�$��23��$��
23,166,0,0q�$
 Wrap=�$	��Top=�$	��120��$��2��$��1���� -   $	 EditGenderCmbx��$
��Left=�$��
108,72,0,0q�$	��Top=�$	��120��
 . $��2���� -    $ EditDataOfBirthTbx��$
��Left=�$��23��$��108,119,0,0q�$
 Wrap=�$	��Top=�$	��120��$��2���� -!   $	 EditMemberShipClassCmbx��$
��Left=�$��108,167,0,0q�$	��Top=�$	��120��
 . $��2���� -"   $	 EditTypeOfFishingCmbx��$
��Left=�$��108,215,0,0q�$	��Top=�$	��120��
 . $��2���� -#   $ EditDateOfJoiningTbx��$
��Left=�$��23��$��
270,72,0,0q�$
 Wrap=�$	��Top=�$	��120��$��2���� -$   $	 EditMemberShipStatusCbmx��$
��Left=�$��270,120,0,0q�$	��Top=�$	��120��
 . $��2���� -%   $ FirstLineOfAddressTbxEdit��$
��Left=�$��23��$��270,167,0,0q�$
 Wrap=�$	��Top=�$	��120��$��2���� -&   $  SecondLineofAddressTbxEdit��$
��Left=�$��23��$��270,215,0,0q�$
 Wrap=�$	��Top=�$	��120��$��2���� -'   $ EditThirdLineAddressTbx��$
��Left=�$��23��$��
420,72,0,0q�$
 Wrap=�$	��Top=�$	��120��$��2���� -(   $ EditPostCodeTbx��$
��Left=�$��23��$��420,120,0,0q�$
 Wrap=�$	��Top=�$	��120��$��2���� -)   $ EditEmailAddressTbx��$
��Left=�$��23��$��420,167,0,0q�$
 Wrap=�$	��Top=�$	��120��$��2���� -*   $ EditCarRegTbx��$
��Left=�$��23��$��420,215,0,0q�$
 Wrap=�$	��Top=�$	��120��$��2���� -+   $ 	UpdateBtn��$��Update��$��2��$
��Left=�$��607,135,0,0q�$	��Top=�$��75���� -,   $ EditTelephoneTbx��$��2��$
��Left=�$��23��$��420,259,0,0q�$
 Wrap=�$	��Top=�$	��120���� --   $ EditNotesTbx��$��2��$
��Left=�$��23��$��420,300,0,0q�$
 Wrap=�$	��Top=�$	��120��